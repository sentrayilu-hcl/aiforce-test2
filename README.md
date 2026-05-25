# aiforce-test2

## Secure Login and Session Management

Implements the `US-001` flow:
- Valid credentials create a secure authenticated session
- Invalid credentials are rejected
- Inactive sessions expire after the configured timeout
- Logout invalidates the active session
- Expired or invalid sessions require re-authentication

Traceability:
`BN-01 -> BR-001 -> EPIC-001 -> US-001`

### Endpoints
- `POST /api/auth/login`
- `POST /api/auth/logout`
- `GET /api/auth/session/validate`
- `GET /api/protected/account`

### Headers
- `Authorization: Bearer <jwt>`
- `X-Session-Id: <session-id>` for logout/validation

### Local configuration
Environment variables:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `SECURITY_JWT_SECRET`
- `SECURITY_JWT_EXPIRATION_MINUTES`
- `SECURITY_SESSION_TIMEOUT_MINUTES`

### Notes
- JWT signing secret must be supplied securely via Kubernetes secrets or external secret manager.
- Passwords are hashed using BCrypt.
- Sessions are tracked in PostgreSQL and invalidated on logout or expiry.
