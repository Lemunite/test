# Keycloak Integration for Microservice (Behind Gateway)

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Application                      │
│                   (Gateway Port 9000)                         │
└──────────────────────────┬──────────────────────────────────┘
                           │
                           │ HTTP Requests
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  API Gateway                                 │
│  ┌────────────────────────────────────────────────────────┐ │
│  │  - Handles Keycloak ROPC OAuth Flow                   │ │
│  │  - Exchanges credentials for JWT tokens               │ │
│  │  - Routes requests to microservices                   │ │
│  │  - Adds Authorization header to requests              │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────┘
                           │
           ┌───────────────┼───────────────┐
           │               │               │
        Port 8081       Port 8082       Port 8083
           │               │               │
┌──────────▼────────┐  ┌──────────┐  ┌──────────┐
│   Microservice    │  │   Other  │  │   Other  │
│   (This Service)  │  │ Services │  │ Services │
│                   │  │          │  │          │
│ ┌───────────────┐ │  │          │  │          │
│ │ JWT Validator │ │  │          │  │          │
│ │ (validates    │ │  │          │  │          │
│ │  tokens from  │ │  │          │  │          │
│ │  gateway)     │ │  │          │  │          │
│ └───────────────┘ │  │          │  │          │
└────────────────────┘  └──────────┘  └──────────┘

                           │
                           │
        ┌──────────────────▼──────────────────┐
        │  Keycloak Server                    │
        │  - Issues JWT tokens                │
        │  - Provides public JWK Set          │
        │  - Manages users and roles          │
        └─────────────────────────────────────┘
```

## How It Works

1. **Frontend** sends credentials to the **Gateway**
2. **Gateway** exchanges credentials with Keycloak for JWT token (ROPC flow)
3. **Gateway** routes API requests to **Microservice** with JWT in `Authorization` header
4. **Microservice** validates JWT token using Keycloak's public JWK Set
5. **Microservice** grants access if token is valid
6. **Microservice** returns protected resources to **Gateway**
7. **Gateway** returns response to **Frontend**

## Key Configuration

### Microservice Configuration (application.yml)

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Keycloak issuer for token validation
          issuer-uri: http://localhost:9080/realms/jhipster
          # Public JWK Set URL for verifying token signatures
          jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

### What the Microservice Does

- **Validates** incoming JWT tokens from the gateway
- **Extracts** roles from token claims
- **Enforces** authorization rules on endpoints
- **Does NOT** issue tokens (gateway does this)

### What the Microservice Does NOT Do

- ❌ Handle user login/authentication (gateway does this)
- ❌ Exchange credentials for tokens (gateway does this)
- ❌ Issue new tokens (Keycloak does this)

## CORS Configuration

The microservice includes CORS configuration to allow requests from:

- `http://localhost:9000` (Gateway frontend)
- `http://localhost:4200` (Angular dev)
- `http://localhost:3000` (React dev)

**For Production:** Update the allowed origins in [SecurityConfiguration.java](src/main/java/com/mycompany/microservice/config/SecurityConfiguration.java).

```java
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "https://your-gateway.com",
    "https://your-domain.com"
));
```

## API Endpoints

### Protected Endpoints (Require Bearer Token)

```bash
# Get patients - requires authentication
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Admin-Only Endpoints

```bash
# Admin operations - requires ADMIN role
curl -X GET http://localhost:8081/api/admin/users \
  -H "Authorization: Bearer <JWT_TOKEN>"
```

### Public Endpoints

```bash
# Health check - no token required
curl -X GET http://localhost:8081/management/health
```

## Authorization (Role-Based Access Control)

Roles are extracted from the JWT token and mapped to Spring authorities:

### From `realm_access.roles`

```json
{
  "realm_access": {
    "roles": ["ADMIN", "USER"]
  }
}
```

### From `resource_access.{clientId}.roles`

```json
{
  "resource_access": {
    "microservice": {
      "roles": ["DOCTOR", "NURSE"]
    }
  }
}
```

Configure in Keycloak which roles users have, and the microservice will automatically enforce them.

## Troubleshooting

### 1. 401 Unauthorized on Microservice Endpoints

**Problem:** Gateway sends request but microservice rejects it

**Causes:**

- Token is missing or invalid
- Token has expired
- `jwk-set-uri` is misconfigured
- Keycloak server is unreachable

**Solution:**

```bash
# Verify token is in request
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <TOKEN>" -v

# Check Keycloak is reachable
curl http://localhost:9080/realms/jhipster/.well-known/openid-configuration

# Check logs for error details
tail -f logs/microservice.log | grep -i security
```

### 2. CORS Errors from Frontend

**Problem:** Frontend gets "CORS policy" error

**Causes:**

- Origin not in allowed list
- Preflight request not handling OPTIONS
- Missing headers in response

**Solution:**

```java
// Update allowed origins in SecurityConfiguration
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "http://localhost:9000"  // Add your gateway URL
));
```

### 3. Roles Not Being Recognized

**Problem:** User has role in Keycloak but endpoint returns 403 Forbidden

**Causes:**

- Role name mismatch (case-sensitive)
- Role assigned at wrong scope (realm vs client)
- Role not in token claims

**Solution:**

```bash
# Decode token to verify roles
# Use jwt.io or jq to inspect token

# Verify role assignment in Keycloak
# Go to Keycloak → Users → {username} → Role Mappings
```

## Gateway Configuration Example

If you're setting up the gateway, it should:

1. **Accept credentials** from frontend
2. **Exchange with Keycloak** for JWT token (ROPC)
3. **Add Authorization header** to microservice requests:
   ```
   Authorization: Bearer <JWT_TOKEN>
   ```
4. **Return token to frontend** for subsequent requests

Example Spring Cloud Gateway configuration:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: microservice
          uri: http://localhost:8081
          predicates:
            - Path=/api/patients/**
          filters:
            - AddRequestHeader=Authorization, Bearer ${jwt-token}
```

## Security Best Practices

1. ✅ **Use HTTPS** in production
2. ✅ **Keep tokens secure** - don't log them
3. ✅ **Set short token lifespan** (5-15 minutes)
4. ✅ **Use refresh tokens** for long-lived sessions
5. ✅ **Validate token signature** (microservice does this automatically)
6. ✅ **Check token expiration** (Spring Security does this)
7. ✅ **Use HTTPS for jwk-set-uri** fetch

## Production Deployment

### Environment Variables

```bash
export KEYCLOAK_AUTH_SERVER_URL=https://keycloak.prod.example.com
export KEYCLOAK_REALM=jhipster
```

### Application Configuration

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: ${KEYCLOAK_AUTH_SERVER_URL}/realms/${KEYCLOAK_REALM}
          jwk-set-uri: ${KEYCLOAK_AUTH_SERVER_URL}/realms/${KEYCLOAK_REALM}/protocol/openid-connect/certs
```

### Security Configuration

```java
// Update CORS for production domain
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "https://yourdomain.com"
));

// Set appropriate max age and methods
configuration.setMaxAge(3600L);
```

## File Structure

```
src/main/java/com/mycompany/microservice/
├── config/
│   ├── SecurityConfiguration.java    ← JWT validation + CORS
│   ├── SecurityJwtConfiguration.java ← Spring Boot auto-config
│   └── WebConfigurer.java
├── security/
│   └── AuthoritiesConstants.java     ← Role constants
└── web/rest/
    └── *Resource.java                ← Protected endpoints

src/main/resources/config/
├── application.yml                    ← Base config
├── application-dev.yml                ← Dev: local Keycloak
└── application-prod.yml               ← Prod: env variables
```

## Testing

### Manual Testing with cURL

```bash
# 1. Get token from gateway
TOKEN=$(curl -s http://localhost:9000/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}' | jq -r '.access_token')

# 2. Use token to access microservice
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN"
```

### Postman Collection

```json
{
  "name": "Microservice API",
  "requests": [
    {
      "name": "Get Patients",
      "method": "GET",
      "url": "http://localhost:8081/api/patients",
      "headers": [
        {
          "key": "Authorization",
          "value": "Bearer {{token}}"
        }
      ]
    }
  ]
}
```

## Related Documentation

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2](https://spring.io/projects/spring-security-oauth2-resource-server)
- [JHipster Gateway](https://www.jhipster.tech/api-gateway/)
- [CORS Configuration](https://developer.mozilla.org/en-US/docs/Web/HTTP/CORS)
