# Keycloak ROPC (Resource Owner Password Credentials) Integration

## Overview

This microservice has been updated to support Keycloak's ROPC (Resource Owner Password Credentials) authentication flow instead of the previous JWT approach. ROPC allows clients to exchange username and password credentials directly for access tokens.

## Architecture Changes

### Previous Flow (JWT-based)

- Custom JWT generation and validation
- Direct token encoding/decoding in the microservice
- Static secret key stored in configuration

### New Flow (Keycloak ROPC)

- Keycloak handles all token generation and validation
- Microservice validates tokens using Keycloak's public JWK Set
- Client exchanges credentials with Keycloak via `/api/authenticate` endpoint
- Microservice validates Bearer tokens in incoming requests

## Configuration

### Development (application-dev.yml)

```yaml
keycloak:
  auth-server-url: http://localhost:9080
  realm: jhipster
  resource: microservice
  credentials:
    secret: your-client-secret-here
  principal-attribute: preferred_username

spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9080/realms/jhipster
          jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

### Production (application-prod.yml)

```yaml
keycloak:
  auth-server-url: ${KEYCLOAK_AUTH_SERVER_URL:http://localhost:9080}
  realm: ${KEYCLOAK_REALM:jhipster}
  resource: ${KEYCLOAK_CLIENT_ID:microservice}
  credentials:
    secret: ${KEYCLOAK_CLIENT_SECRET:}
  principal-attribute: preferred_username
```

Use environment variables in production:

- `KEYCLOAK_AUTH_SERVER_URL` - Keycloak server URL (e.g., https://keycloak.example.com)
- `KEYCLOAK_REALM` - Keycloak realm name
- `KEYCLOAK_CLIENT_ID` - Keycloak client ID
- `KEYCLOAK_CLIENT_SECRET` - Keycloak client secret

## API Endpoints

### Authenticate (ROPC Flow)

**Request:**

```bash
POST /api/authenticate
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}
```

**Response (Success - 200 OK):**

```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cC...",
  "token_type": "Bearer"
}
```

**Response (Failure - 401 Unauthorized):**

```json
{}
```

### Using the Token

Include the token in subsequent requests:

```bash
GET /api/patients
Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cC...
```

## Keycloak Server Setup

### Create a Client in Keycloak

1. Log in to Keycloak admin console
2. Navigate to your realm (e.g., "jhipster")
3. Go to Clients → Create
4. Set Client ID to "microservice"
5. Enable "Standard flow Enabled" and "Direct access grants enabled" (for ROPC)
6. Set Valid Redirect URIs to: `http://localhost:8081/*`
7. Get the client credentials from the "Credentials" tab

### Configure Roles

1. Create roles in the realm or client (e.g., `ADMIN`, `USER`)
2. Map roles to users
3. Roles will be available in token's `realm_access.roles` or `resource_access.{clientId}.roles`

## Code Changes

### New Files Created

1. **KeycloakTokenProvider.java** - Service for ROPC token exchange with Keycloak
2. **AuthenticateController.java** - REST endpoint for authentication
3. **RestTemplateConfiguration.java** - Configuration for HTTP client

### Modified Files

1. **SecurityConfiguration.java** - Updated role converter to support Keycloak tokens
2. **SecurityJwtConfiguration.java** - Simplified to use Keycloak's JWK Set
3. **application-dev.yml** - Added Keycloak configuration
4. **application-prod.yml** - Added Keycloak configuration with environment variables
5. **pom.xml** - Added Keycloak and WebFlux dependencies

## Dependencies Added

```xml
<!-- Keycloak Spring Boot Adapter -->
<dependency>
    <groupId>org.keycloak</groupId>
    <artifactId>keycloak-spring-boot-starter</artifactId>
    <version>24.0.0</version>
</dependency>

<!-- Spring WebFlux for async HTTP clients -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

## Testing

### Test ROPC Flow

```bash
# Get access token
curl -X POST http://localhost:8081/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Use token to access protected endpoint
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer {access_token}"
```

### Test with cURL

```bash
# Extract token from response
TOKEN=$(curl -s -X POST http://localhost:8081/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}' \
  | jq -r '.access_token')

# Use token
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN"
```

## Security Considerations

1. **HTTPS Only**: Always use HTTPS in production
2. **Client Secret**: Store client secret securely using environment variables
3. **Token Scope**: Requests include `openid profile email` scopes
4. **Token Validation**: Tokens are validated against Keycloak's JWK Set
5. **Role Mapping**: Roles are extracted from JWT claims and used for authorization

## Migration from Old JWT to ROPC

If you have existing JWT-based authentication:

1. The microservice now only validates tokens, not generates them
2. Gateway should use `/api/authenticate` endpoint to get tokens
3. Frontend should exchange credentials with `/api/authenticate`, not the old JWT endpoint
4. Existing bearer token validation still works (backward compatible during transition)

## Troubleshooting

### "Invalid signature" errors

- Ensure `jwk-set-uri` is configured correctly
- Check that Keycloak server is accessible
- Verify token issuer matches `issuer-uri`

### Authentication fails with 401

- Verify username and password are correct
- Check Keycloak client credentials in configuration
- Ensure ROPC ("Direct access grants") is enabled on Keycloak client
- Check Keycloak server logs

### Roles not appearing

- Verify roles are assigned to user in Keycloak
- Check role name matches what application expects
- Verify roles are in `realm_access` or `resource_access` claims

## References

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [OAuth 2.0 Resource Owner Password Credentials Flow](https://www.rfc-editor.org/rfc/rfc6749#section-4.3.2)
- [Spring Security OAuth2 Resource Server](https://spring.io/projects/spring-security-oauth2-resource-server)
- [JHipster Security Documentation](https://www.jhipster.tech/security/)
