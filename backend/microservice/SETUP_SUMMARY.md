# Migration Summary: Backend to Accept Keycloak Tokens from Gateway

## Overview

Your microservice has been reconfigured to properly work with a JHipster Gateway that handles Keycloak ROPC authentication. The gateway performs the OAuth2 login and forwards requests to the microservice with a JWT bearer token.

## What Changed

### 1. Security Configuration Updated (SecurityConfiguration.java)

**Added:**

- CORS configuration to allow requests from gateway frontend
- Support for validating Keycloak-issued JWT tokens
- Proper authority extraction from token claims

**Removed:**

- Unnecessary ROPC authentication endpoint
- Password credential handling

### 2. JWT Validation Simplified (SecurityJwtConfiguration.java)

- Now relies on Spring Boot auto-configuration
- Validates tokens using Keycloak's public JWK Set
- No token generation (Keycloak handles this)

### 3. Dependencies Updated (pom.xml)

- Added `keycloak-spring-boot-starter` for Keycloak integration
- Kept existing Spring Security OAuth2 dependencies

### 4. Configuration Updated (application-dev.yml, application-prod.yml)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9080/realms/jhipster
          jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

## Architecture Flow

```
Frontend (Gateway Port 9000)
         ↓
    API Gateway
    (handles Keycloak ROPC)
         ↓
   Adds JWT Token
    Authorization: Bearer <TOKEN>
         ↓
    Microservice (Port 8081)
    (validates token)
         ↓
   Protected Resources
```

## How to Use

### 1. Ensure Gateway is Configured

Your gateway must:

- Exchange user credentials with Keycloak for JWT token (ROPC flow)
- Add `Authorization: Bearer <token>` header to microservice requests
- Forward requests to microservice

### 2. Microservice Configuration

The microservice automatically validates tokens. Ensure `application-dev.yml` has:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: http://localhost:9080/realms/jhipster
          jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

### 3. Access Protected Resources

Gateway sends request with token:

```bash
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIs..."
```

Microservice validates and returns resource if authorized.

## CORS Configuration

Updated to allow requests from:

- `http://localhost:9000` (Gateway)
- `http://localhost:4200` (Angular)
- `http://localhost:3000` (React)

**For production**, update `SecurityConfiguration.java`:

```java
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "https://yourdomain.com"
));
```

## Security Rules

Endpoints are protected as follows:

| Endpoint             | Rule                    |
| -------------------- | ----------------------- |
| `/api/admin/**`      | Requires ADMIN role     |
| `/api/**`            | Requires authentication |
| `/management/**`     | Requires ADMIN role     |
| `/management/health` | Public                  |
| `/management/info`   | Public                  |

## Roles from Keycloak

Roles are extracted from JWT claims:

### Realm Roles

```json
{
  "realm_access": {
    "roles": ["ADMIN", "USER"]
  }
}
```

### Client-Specific Roles

```json
{
  "resource_access": {
    "microservice": {
      "roles": ["DOCTOR", "NURSE"]
    }
  }
}
```

## Troubleshooting

### Frontend can't access microservice endpoints

1. **Check CORS configuration**

   - Verify origin is in allowed list
   - Check that OPTIONS requests are handled

2. **Check token validation**

   - Verify gateway is sending valid token
   - Check microservice logs for validation errors
   - Verify Keycloak server is reachable

3. **Check routing**
   - Verify gateway routes to correct microservice URL
   - Check firewall rules

### Token validation fails (401 Unauthorized)

1. **Verify Keycloak is running**

   ```bash
   curl http://localhost:9080/realms/jhipster/.well-known/openid-configuration
   ```

2. **Check token issuer matches**

   - Token issuer must match `issuer-uri` in config

3. **Check JWK Set is accessible**
   ```bash
   curl http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
   ```

### Roles not recognized (403 Forbidden)

1. **Assign role in Keycloak**

   - Go to Keycloak → Users → {user} → Role Mappings

2. **Verify role name**

   - Role names are case-sensitive

3. **Inspect token claims**
   - Decode token at jwt.io to verify roles

## Files Changed

### New/Modified

- `src/main/java/config/SecurityConfiguration.java` - Added CORS, updated security rules
- `src/main/java/config/SecurityJwtConfiguration.java` - Simplified for auto-config
- `src/main/resources/config/application-dev.yml` - Updated config
- `src/main/resources/config/application-prod.yml` - Updated config
- `pom.xml` - Added Keycloak dependency

### Removed

- `AuthenticateController.java` - Not needed (gateway handles auth)
- `KeycloakTokenProvider.java` - Not needed (gateway handles token exchange)
- `RestTemplateConfiguration.java` - Not needed

## Production Deployment

### Environment Variables

Set in production environment:

```bash
KEYCLOAK_AUTH_SERVER_URL=https://keycloak.yourdomain.com
KEYCLOAK_REALM=jhipster
```

### Application Start

```bash
java -jar microservice.jar \
  --spring.security.oauth2.resourceserver.jwt.issuer-uri=$KEYCLOAK_AUTH_SERVER_URL/realms/$KEYCLOAK_REALM \
  --spring.security.oauth2.resourceserver.jwt.jwk-set-uri=$KEYCLOAK_AUTH_SERVER_URL/realms/$KEYCLOAK_REALM/protocol/openid-connect/certs
```

## What's Different from Previous Setup

### Before

- Custom JWT encoding in microservice
- Shared secret for token validation
- `/api/authenticate` endpoint on microservice
- Token issued by microservice

### Now

- JWT tokens issued by Keycloak only
- Public JWK Set for token validation
- Gateway handles all authentication
- Microservice only validates tokens
- Gateway adds Authorization header

## Next Steps

1. ✅ Microservice is now configured to accept Keycloak tokens
2. ✅ CORS enabled for gateway frontend
3. Verify gateway is sending tokens correctly
4. Test end-to-end flow through gateway
5. Update production Keycloak URLs
6. Deploy both gateway and microservice

## Testing

### From Gateway

```bash
# Gateway should send this to microservice
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <gateway-token>"
```

### Direct to Microservice (for testing)

Get token manually from Keycloak:

```bash
TOKEN=$(curl -s -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=microservice&client_secret=SECRET&username=admin&password=admin" \
  | jq -r '.access_token')

# Then use it
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN"
```

## Support

For issues:

1. Check microservice logs: `tail -f logs/microservice.log`
2. Check gateway logs for token exchange
3. Verify Keycloak server is running and accessible
4. Check network connectivity between services
5. Verify configuration matches your Keycloak setup
