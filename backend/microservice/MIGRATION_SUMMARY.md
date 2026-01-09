# Migration Summary: JWT to Keycloak ROPC

## Overview

Your microservice has been successfully migrated from a custom JWT implementation to Keycloak's Resource Owner Password Credentials (ROPC) authentication flow. This allows for centralized authentication and authorization through Keycloak.

## What Changed

### 1. Dependencies Added (pom.xml)

- `keycloak-spring-boot-starter` v24.0.0 - For Keycloak integration
- `spring-boot-starter-webflux` - For async HTTP client support

### 2. New Java Classes Created

#### KeycloakTokenProvider.java

- Handles ROPC token exchange with Keycloak
- Methods:
  - `obtainAccessToken(username, password)` - Get access token using credentials
  - `refreshAccessToken(refreshToken)` - Refresh an expired token

#### AuthenticateController.java

- New REST endpoint: `POST /api/authenticate`
- Accepts username/password in JSON
- Returns access token and token type
- Public endpoint (no authentication required)

#### RestTemplateConfiguration.java

- Configures HTTP client for Keycloak communication
- 5-second connection timeout
- 10-second read timeout

### 3. Modified Java Classes

#### SecurityConfiguration.java

- Enhanced `KeycloakGrantedAuthoritiesConverter` to handle both:
  - `realm_access.roles` (realm-level roles)
  - `resource_access.{clientId}.roles` (client-specific roles)
- Added comments about ROPC support
- Security rules remain the same

#### SecurityJwtConfiguration.java

- Simplified to rely on Spring Boot auto-configuration
- Removed custom JWT encoding (Keycloak handles this now)
- Removed SecurityMetersService dependency
- Uses Keycloak's JWK Set for token validation

### 4. Configuration Changes

#### application-dev.yml

Added:

```yaml
keycloak:
  auth-server-url: http://localhost:9080
  realm: jhipster
  resource: microservice
  credentials:
    secret: your-client-secret-here
  principal-attribute: preferred_username

spring.security.oauth2.resourceserver.jwt:
  issuer-uri: http://localhost:9080/realms/jhipster
  jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

#### application-prod.yml

Added environment variable support:

```yaml
keycloak:
  auth-server-url: ${KEYCLOAK_AUTH_SERVER_URL:http://localhost:9080}
  realm: ${KEYCLOAK_REALM:jhipster}
  resource: ${KEYCLOAK_CLIENT_ID:microservice}
  credentials:
    secret: ${KEYCLOAK_CLIENT_SECRET:}
  principal-attribute: preferred_username
```

Removed JWT base64-secret configuration (no longer needed).

## How to Use

### 1. Keycloak Setup (if not already done)

1. Create a client named "microservice" in Keycloak
2. Enable "Standard flow" and "Direct access grants"
3. Get the client secret from Credentials tab
4. Create roles in your realm (e.g., ADMIN, USER)
5. Assign roles to users

### 2. Configuration

**For Development:**

- Update `application-dev.yml`:
  - Change `client.secret` to your actual Keycloak client secret

**For Production:**

- Set environment variables:
  ```bash
  export KEYCLOAK_AUTH_SERVER_URL=https://keycloak.example.com
  export KEYCLOAK_REALM=jhipster
  export KEYCLOAK_CLIENT_ID=microservice
  export KEYCLOAK_CLIENT_SECRET=<secret-from-keycloak>
  ```

### 3. Client Usage (Frontend/Gateway)

#### Get Access Token

```bash
curl -X POST http://localhost:8081/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{
    "username": "user@example.com",
    "password": "password123"
  }'
```

Response:

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
  "token_type": "Bearer"
}
```

#### Use Access Token

```bash
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <access_token>"
```

## Backward Compatibility

The changes are mostly backward compatible:

- Existing JWT validation logic still works
- Bearer token endpoints accept Keycloak tokens
- Authorization checks remain the same
- API endpoints unchanged (except new `/api/authenticate`)

## Security Improvements

1. **Centralized Authentication**: Keycloak manages all user credentials
2. **Better Token Validation**: Uses Keycloak's JWK Set instead of shared secret
3. **Role Management**: Centralized role management in Keycloak
4. **Token Refresh**: Built-in token refresh mechanism
5. **Multi-tenancy Support**: Keycloak handles multiple realms

## Removed/Deprecated

- Custom JWT encoding and decoding logic
- Static JWT base64 secrets in microservice configuration
- JwtEncoder bean (Keycloak generates tokens)
- SecurityMetersService dependency in JWT configuration

## Next Steps

1. Update your gateway/frontend to use `/api/authenticate` endpoint
2. Test ROPC flow with curl or Postman
3. Configure Keycloak roles and user assignments
4. Set up environment variables for production
5. Deploy and test authentication flow
6. Monitor Keycloak server logs for any issues

## Troubleshooting

### Issue: "Invalid signature" on token validation

- Check `jwk-set-uri` is accessible from microservice
- Verify token issuer matches configured `issuer-uri`
- Ensure Keycloak server is running

### Issue: Authentication returns 401

- Verify username/password are correct
- Check `KEYCLOAK_CLIENT_SECRET` is set correctly
- Ensure "Direct access grants" is enabled on Keycloak client
- Check Keycloak logs for detailed errors

### Issue: Roles not being recognized

- Verify roles are assigned to user in Keycloak
- Check role name matches your application's expectations
- Roles should appear in JWT token claims

## Documentation

See `KEYCLOAK_ROPC_SETUP.md` for detailed setup and configuration guide.
