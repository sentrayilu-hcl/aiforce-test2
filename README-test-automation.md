# Auth API Automation

Traceability:
BN-01 -> BR-001 -> EPIC-001 -> US-001

## Prerequisites
- Java 21
- Maven 3.9+
- Application running locally or in K8s
- Valid environment variables configured

## Environment Variables
- BASE_URL
- TEST_USERNAME
- TEST_PASSWORD
- TEST_INVALID_PASSWORD
- SESSION_WAIT_SECONDS

## Run
```bash
chmod +x scripts/run-auth-api-tests.sh
./scripts/run-auth-api-tests.sh
```

## Coverage
- Login success/failure
- Session validation
- Logout invalidation
- Protected endpoint authorization
- Re-login after logout
