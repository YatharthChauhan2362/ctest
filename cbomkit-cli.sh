#!/bin/bash

# Default values
TIMEOUT=300  # 5 minutes timeout
POLL_INTERVAL=2

# Function to check if a command exists
command_exists() {
    command -v "$1" >/dev/null 2>&1
}

# Check required dependencies
if ! command_exists docker && ! command_exists podman; then
    echo "Error: Neither docker nor podman is installed" >&2
    exit 1
fi

if ! command_exists websocat; then
    echo "Error: websocat is not installed" >&2
    echo "Please install websocat first:" >&2
    echo "  On Ubuntu/Debian: sudo apt-get install websocat" >&2
    echo "  On macOS: brew install websocat" >&2
    exit 1
fi

# Determine container engine
if command_exists docker; then
    ENGINE="docker"
elif command_exists podman; then
    ENGINE="podman"
fi

# Function to wait for service readiness
wait_for_service() {
    local service_name="$1"
    local check_command="$2"
    local timeout="$3"
    local interval="$4"
    local elapsed=0

    echo "Waiting for $service_name to be ready..."
    
    while [ $elapsed -lt $timeout ]; do
        if eval "$check_command" >/dev/null 2>&1; then
            echo "$service_name is ready!"
            return 0
        fi
        sleep "$interval"
        elapsed=$((elapsed + interval))
    done

    echo "Timeout waiting for $service_name" >&2
    return 1
}

# Function to cleanup on exit
cleanup() {
    echo "Cleaning up..."
    $ENGINE compose down >/dev/null 2>&1
}

# Register cleanup function
trap cleanup EXIT

# Check arguments
if [ $# -lt 1 ]; then
    echo "Usage: $0 <git-repository-url>" >&2
    exit 1
fi

REPO_URL="$1"

# Start services
$ENGINE compose up -d db backend

# Wait for backend to be ready
wait_for_service "CBOMkit backend" "curl -s http://localhost:8081/api" $TIMEOUT $POLL_INTERVAL || exit 1

# Wait for PostgreSQL to be ready
wait_for_service "PostgreSQL" "$ENGINE compose exec db pg_isready -U cbomkit" $TIMEOUT $POLL_INTERVAL || exit 1

# Generate unique client ID
CLIENT_ID=$(uuidgen || cat /proc/sys/kernel/random/uuid)

echo "Generating CBOM for $REPO_URL..."

# Prepare JSON payload with correct field name (gitUrl instead of scanUrl)
JSON_PAYLOAD="{\"gitUrl\":\"$REPO_URL\"}"

# Connect to WebSocket and send scan request
(echo "$JSON_PAYLOAD"; sleep 1) | websocat "ws://localhost:8081/v1/scan/$CLIENT_ID" | while read -r line; do
    if echo "$line" | grep -q "ERROR"; then
        echo "Error during scan: $line" >&2
        exit 1
    fi
    if echo "$line" | grep -q "\"type\":\"CBOM\""; then
        echo "$line" | jq -r '.message' > cbom.json
        echo "CBOM generation complete! Results saved to cbom.json"
        exit 0
    fi
done

# Check if cbom.json was created
if [ ! -f cbom.json ]; then
    echo "Error: CBOM generation failed" >&2
    exit 1
fi