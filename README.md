# CBOMkit - The Essentials for CBOMs

[![License](https://img.shields.io/github/license/IBM/cbomkit.svg?)](https://opensource.org/licenses/Apache-2.0)
[![Current Release](https://img.shields.io/github/release/IBM/cbomkit.svg?logo=IBM)](https://github.com/IBM/cbomkit/releases)

CBOMkit is a comprehensive toolset for handling Cryptography Bill of Materials (CBOM). It provides:

- **CBOM Generation**: Generate CBOMs from source code by scanning private and public git repositories to detect cryptographic usage
- **CBOM Viewer**: Visualize and analyze generated or uploaded CBOMs with detailed statistics
- **CBOM Compliance Check**: Evaluate CBOMs against specified compliance policies
- **CBOM Database**: Store and manage CBOMs with a RESTful API

## Quick Start

### Using Docker Compose

```bash
# Clone the repository
git clone https://github.com/IBM/cbomkit

# Start with Docker
make production

# Or use Podman instead
make production ENGINE=podman
```

> **Note**: The service will be available at http://localhost:8001

### Using CLI

See [CLI.md](CLI.md) for command-line usage instructions.

## Components

### Frontend & CBOMkit-coeus

The web interface provides:
- Browse existing CBOMs
- Generate new CBOMs via scanning
- Upload & visualize CBOMs
- Analyze compliance

For visualization-only usage, deploy CBOMkit-coeus:

```bash
make coeus
```

### API Server

The API server provides:
- RESTful API for CBOM management
- WebSocket integration for real-time scan updates
- Compliance checking capabilities

See [OpenAPI specification](openapi.yaml) for full API details.

### Compliance

CBOMkit includes compliance checking for:
- Basic quantum-safe verification
- Extensible policy framework
- Multiple deployment configurations

| Deployment | Compliance Check Method |
|------------|------------------------|
| `coeus` | Client-side basic quantum resistance check |
| `production` | Backend core compliance service |
| `ext-compliance` | External dedicated compliance service |

### Scanning Capabilities

Powered by [CBOMkit-hyperion](https://github.com/IBM/sonar-cryptography), current scanning supports:

| Language | Library | Coverage |
|----------|---------|-----------|
| Java | [JCA](https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html) | 100% |
| Java | [BouncyCastle](https://github.com/bcgit/bc-java) (light-weight API) | 100% |
| Python | [pyca/cryptography](https://cryptography.io/en/latest/) | 100% |

## Deployment

### Helm Chart

Deploy to Kubernetes:

```bash
helm install cbomkit \
  --set common.clusterDomain={CLUSTER_DOMAIN} \
  --set postgresql.auth.username={POSTGRES_USER} \
  --set postgresql.auth.password={POSTGRES_PASSWORD} \
  --set backend.tag=$(curl -s https://api.github.com/repos/IBM/cbomkit/releases/latest | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/') \
  --set frontend.tag=$(curl -s https://api.github.com/repos/IBM/cbomkit/releases/latest | grep '"tag_name":' | sed -E 's/.*"([^"]+)".*/\1/') \
  ./chart
```

## Contributing

Please read:
- [Contributing Guidelines](CONTRIBUTING.md)
- [Code of Conduct](CODE_OF_CONDUCT.md)

## License

[Apache License 2.0](LICENSE.txt)