# Fixing: "Frontend at Gateway Can't Access Microservice Frontend"

## Problem Summary

The frontend running on the gateway (typically port 9000) cannot access the microservice APIs (port 8081).

## Root Causes & Solutions

### 1. CORS Issue (Most Common)

**Symptom:** Browser console shows:

```
Access to XMLHttpRequest at 'http://localhost:8081/api/patients' from origin 'http://localhost:9000'
has been blocked by CORS policy
```

**Solution:** ✅ FIXED in SecurityConfiguration.java

The microservice now includes CORS configuration:

```java
CorsConfiguration configuration = new CorsConfiguration();
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "http://localhost:9000",  // Gateway frontend
    "http://localhost:4200",  // Angular dev
    "http://localhost:3000"   // React dev
));
configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
configuration.setAllowedHeaders(java.util.Arrays.asList("*"));
configuration.setAllowCredentials(true);
```

**For Production:** Update the allowed origins with your actual domain.

### 2. Missing or Invalid Authorization Header

**Symptom:** 401 Unauthorized responses

**Solution:** ✅ FIXED in SecurityConfiguration.java

The gateway must send JWT token in Authorization header:

```
Authorization: Bearer <JWT_TOKEN>
```

The microservice validates this token against Keycloak's JWK Set.

**Check:** Verify gateway is adding the token header to requests.

### 3. Gateway Routes Configuration

**Symptom:** 404 Not Found or requests never reach microservice

**Solution:** Update your Gateway's application.yml:

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: microservice
          uri: http://localhost:8081
          predicates:
            - Path=/api/**
          filters:
            - AuthorizationHeaderFilter # Your auth filter
```

### 4. Network Connectivity Issue

**Symptom:** Connection timeouts or refused connections

**Check:**

```bash
# From gateway server, verify microservice is reachable
curl -X GET http://localhost:8081/management/health

# Should return:
{
  "status": "UP"
}
```

### 5. Keycloak Token Validation

**Symptom:** Valid token from Keycloak rejected by microservice

**Solution:** ✅ FIXED in SecurityConfiguration + SecurityJwtConfiguration.java

Verify configuration:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          # Token issuer must match
          issuer-uri: http://localhost:9080/realms/jhipster
          # JWK Set must be accessible
          jwk-set-uri: http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

**Test Keycloak connectivity:**

```bash
# Verify JWK Set is accessible
curl http://localhost:9080/realms/jhipster/protocol/openid-connect/certs
```

## Complete Setup Checklist

### ✅ Microservice Configuration

- [x] CORS enabled for gateway origin
- [x] JWT validation configured
- [x] Security rules set up
- [x] Keycloak issuer-uri configured
- [x] JWK Set URI configured

### ✅ Gateway Configuration

Your gateway needs to:

- [ ] Accept username/password from frontend
- [ ] Exchange credentials with Keycloak (ROPC)
- [ ] Get JWT token from Keycloak
- [ ] Add `Authorization: Bearer <token>` to requests
- [ ] Route to microservice

### ✅ Keycloak Configuration

- [ ] Realm exists (e.g., "jhipster")
- [ ] Client exists (e.g., "gateway" or "microservice")
- [ ] Direct access grants enabled
- [ ] Users created with passwords
- [ ] Roles assigned to users

## Testing the Connection

### Step 1: Test Keycloak

```bash
# Verify Keycloak is running
curl http://localhost:9080/realms/jhipster
```

### Step 2: Test Microservice Health

```bash
# Should work without token
curl http://localhost:8081/management/health
```

### Step 3: Test With Token

```bash
# Get token from Keycloak
TOKEN=$(curl -s -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=microservice&client_secret=YOUR_SECRET&username=admin&password=admin" \
  | jq -r '.access_token')

# Access protected resource
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer $TOKEN"
```

### Step 4: Test From Gateway

```bash
# Gateway sends request with token to microservice
# This should work if all above steps pass

# From your gateway application, test:
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <GATEWAY_TOKEN>"
```

## Common Errors & Fixes

| Error              | Cause                                     | Fix                                        |
| ------------------ | ----------------------------------------- | ------------------------------------------ |
| CORS blocked       | Microservice doesn't allow gateway origin | Update CORS allowed origins                |
| 401 Unauthorized   | Invalid or missing token                  | Gateway must send Authorization header     |
| 403 Forbidden      | Token valid but user lacks required role  | Assign proper roles in Keycloak            |
| Connection refused | Microservice not running or wrong port    | Check microservice is running on port 8081 |
| Invalid signature  | Token issuer mismatch                     | Verify issuer-uri matches Keycloak         |
| Invalid token      | JWK Set error                             | Verify jwk-set-uri is accessible           |

## Configuration Files Modified

### 1. SecurityConfiguration.java

- Added CORS configuration bean
- Added CORS filter to security chain
- Updated security rules

### 2. application-dev.yml

- Configured OAuth2 resource server
- Set Keycloak issuer and JWK Set URLs

### 3. application-prod.yml

- Added environment variable support for Keycloak URLs

### 4. pom.xml

- Added Keycloak Spring Boot Starter
- Kept OAuth2 resource server dependency

## Production Deployment

### Environment Variables

```bash
export KEYCLOAK_AUTH_SERVER_URL=https://keycloak.yourdomain.com
export KEYCLOAK_REALM=yourrealm
export GATEWAY_URL=https://gateway.yourdomain.com
```

### Update CORS

Edit [SecurityConfiguration.java](src/main/java/com/mycompany/microservice/config/SecurityConfiguration.java):

```java
configuration.setAllowedOrigins(java.util.Arrays.asList(
    "https://gateway.yourdomain.com"  // Production gateway URL
));
```

### Deploy

```bash
java -Dspring.config.location=file:/etc/config/application-prod.yml \
  -jar microservice.jar
```

## Still Not Working?

### Collect Debug Info

```bash
# Microservice logs
tail -f logs/microservice.log

# Check debug level
grep -i "cors\|authorization\|jwt" logs/microservice.log

# Keycloak logs
docker logs keycloak

# Network test
netstat -an | grep 8081
```

### Enable Debug Logging

Add to application-dev.yml:

```yaml
logging:
  level:
    org.springframework.security: DEBUG
    org.springframework.web.cors: DEBUG
```

### Contact Information

If issues persist:

1. Check microservice logs for security errors
2. Check gateway logs for token exchange errors
3. Verify Keycloak server is running
4. Verify network connectivity between services
5. Review configuration in application.yml files
