# CBOMkit CLI Usage

The CBOMkit CLI tool provides a command-line interface for generating CBOMs from Git repositories.

## Prerequisites

- Docker or Podman
- websocat
- Git

## Installation

The CLI tool is included in the repository as `cbomkit-cli.sh`. Make it executable:

```bash
chmod +x cbomkit-cli.sh
```

## Usage

Basic syntax:

```bash
./cbomkit-cli.sh <git-repository-url>
```

The tool will:
1. Start required services (PostgreSQL, CBOMkit backend)
2. Clone and scan the repository
3. Generate a CBOM
4. Save results to `cbom.json`

### Example

```bash
./cbomkit-cli.sh https://github.com/apache/kafka
```

## Environment Variables

- `TIMEOUT`: Maximum wait time in seconds (default: 300)
- `POLL_INTERVAL`: Status check interval in seconds (default: 2)

## Container Engine

The CLI automatically detects and uses either Docker or Podman. Docker is preferred if both are available.

## Output

The generated CBOM is saved as `cbom.json` in the current directory. The file contains:
- Cryptographic assets detected
- Dependencies
- Metadata
- Evidence of usage

## Error Handling

The CLI provides error messages for:
- Missing dependencies
- Connection failures
- Scan errors
- Invalid repositories

## Cleanup

The tool automatically cleans up:
- Temporary containers
- Cloned repositories
- Temporary files

Services are stopped when the scan completes or if an error occurs.