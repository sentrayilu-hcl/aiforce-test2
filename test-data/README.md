# US-001 Test Data Package

Traceability:
BN-01 -> BR-001 -> EPIC-001 -> US-001

## Contents
- SQL seed data for active, inactive, expired, revoked, and boundary session states
- JSON fixtures for login, logout, validation, and protected-access flows
- Cleanup scripts for test reset
- Validation script for fixture integrity

## Safety
- Synthetic data only
- No secrets or production credentials
- No real customer records
- No plaintext passwords stored in repository files

## Usage
### Seed data
```bash
./test-data/scripts/reset_and_seed_us001.sh
```

### Validate fixtures
```bash
./test-data/scripts/run_data_validation.sh
```

## Notes for Kubernetes
Use the SQL scripts in an init container or job against a test database only.
