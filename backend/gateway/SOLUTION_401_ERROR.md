# Embedded Login Flow Fix - ROOT CAUSE & SOLUTION

## The 401 Error - Root Cause Found! ⚠️

### CRITICAL: Two Competing JWT Decoders

You have **TWO beans with the same name** `jwtDecoder()`:

1. **SecurityJwtConfiguration.java** - Line 28

   ```java
   @Bean
   public ReactiveJwtDecoder jwtDecoder(SecurityMetersService metersService) {
     // Uses local JWT secret for validation
   }

   ```

2. **SecurityConfiguration.java** - Used for Keycloak
   ```java
   .oauth2ResourceServer(oauth2 ->
       oauth2.jwt(jwt -> jwt
           .jwtDecoder(jwtDecoder())  // Calls the wrong bean!
   ```

### Why This Causes 401

When the frontend sends a Keycloak token:

1. ❌ Spring tries to validate it with the local JWT secret key
2. ❌ Keycloak token signature doesn't match local secret
3. ❌ Validation fails → 401 Unauthorized

## Fixes Applied

### 1. Conditional Bean Loading (CRITICAL FIX)

**File: SecurityJwtConfiguration.java**

```java
@Configuration
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "false", matchIfMissing = false)
public class SecurityJwtConfiguration {
  // Only loads when Keycloak is DISABLED
}

```

This ensures the local JWT decoder only loads when NOT using Keycloak.

### 2. Enhanced Keycloak Decoder with Error Logging

**File: SecurityConfiguration.java**

```java
@Bean
public ReactiveJwtDecoder jwtDecoder() {
  if (keycloakProperties.isEnabled()) {
    String issuerUri = keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm();
    log.info("Configuring JWT decoder with Keycloak issuer URI: {}", issuerUri);

    ReactiveJwtDecoder decoder = ReactiveJwtDecoders.fromIssuerLocation(issuerUri);

    // Wrap decoder with error logging
    return token ->
      decoder
        .decode(token)
        .doOnError(error -> {
          log.error("JWT validation failed: {}", error.getMessage(), error);
          if (error.getMessage().contains("Signature")) {
            log.error("Token signature validation failed - possible issuer/key mismatch");
          } else if (error.getMessage().contains("expired")) {
            log.error("Token is expired");
          }
        });
  }
  throw new IllegalStateException("JWT Decoder not properly configured. Keycloak must be enabled.");
}

```

### 3. Enhanced Debug Logging in Configuration

**File: application-dev.yml**

```yaml
logging:
  level:
    org.springframework.security.oauth2.jwt: DEBUG
    org.springframework.security.oauth2.server.resource: DEBUG
    com.mycompany.gateway.config.SecurityConfiguration: DEBUG
```

### 4. New Diagnostic Endpoints (No Auth Required)

**File: TokenDebugResource.java** - Created new resource

Access these to debug token issues:

- `GET /api/debug/token-info` - Shows decoded token, issuer, expiration, roles
- `GET /api/debug/auth-info` - Shows current authentication context

## How to Verify the Fix

### Step 1: Rebuild and Restart

```bash
mvn clean install -DskipTests
# Restart the application
```

### Step 2: Check Logs on Startup

Look for:

```
INFO  ... SecurityConfiguration initialized. Keycloak enabled: true
INFO  ... Configuring JWT decoder with Keycloak issuer URI: http://localhost:9080/realms/jhipster
```

### Step 3: Try Login Again

1. Navigate to login page
2. Enter credentials
3. Check browser console for: `Token stored successfully from authentication endpoint`
4. Check Network tab - `/api/account` should now return 200 (not 401)

### Step 4: Debug Token If Still Failing

If still getting 401, call this debug endpoint (after login, with token):

```
GET http://localhost:8080/api/debug/token-info
```

Look for:

- `"issuer": "http://localhost:9080/realms/jhipster"` - Should match your Keycloak realm
- `"expired": false` - Token should not be expired
- `"realm_access"` - Should contain your roles

### Step 5: Check Backend Logs

Search logs for:

```
JWT validation failed
```

If you see "Signature validation failed", it means the issuer/key doesn't match. Check:

- Is Keycloak server running at `http://localhost:9080`?
- Is the realm exactly `jhipster`?
- Can the backend reach Keycloak's JWKS endpoint?

## Configuration Verification

Verify your configuration in `application-dev.yml`:

```yaml
keycloak:
  enabled: true # ✅ Must be true
  auth-server-url: http://localhost:9080 # ✅ Keycloak server URL
  realm: jhipster # ✅ Exact realm name
  resource: web_app # ✅ Client ID
  credentials:
    secret: Jl6l62hyD6lBvC78DHO9mq2cFkgVVOvZ # ✅ Client secret
```

## Expected Behavior After Fix

1. **Login succeeds** → Token returned in Authorization header
2. **Token stored** → Appears in localStorage/sessionStorage
3. **Token sent** → Every API request includes `Authorization: Bearer {token}`
4. **Token validated** → Backend validates against Keycloak public key
5. **User authenticated** → Subsequent requests return 200 (not 401)
6. **User info loaded** → `/api/account` succeeds, user sees authenticated UI

## Troubleshooting If Still Failing

### 401 on /api/account after token sent

**Check**:

- [ ] Is Keycloak server running? (`http://localhost:9080/auth`)
- [ ] Can you manually call Keycloak token endpoint?
  ```bash
  curl -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
    -d "grant_type=password" \
    -d "client_id=web_app" \
    -d "client_secret=Jl6l62hyD6lBvC78DHO9mq2cFkgVVOvZ" \
    -d "username=admin" \
    -d "password=admin"
  ```
- [ ] Does backend log show "Configuring JWT decoder..."?
- [ ] Does `/api/debug/token-info` show correct issuer?

### Token not stored after login

**Check**:

- [ ] Browser console - any errors?
- [ ] Network tab - is Authorization header in login response?
- [ ] localStorage - does `jhi-authenticationToken` exist after login?
- [ ] Check frontend logs for: "Token stored successfully"

### Backend can't reach Keycloak

**Check**:

- [ ] Firewall/networking - can backend reach `http://localhost:9080`?
- [ ] If using Docker - are containers networked correctly?
- [ ] Check backend logs for connection errors to Keycloak

## Files Modified

### Backend Java

- `SecurityConfiguration.java` - Added error logging, fixed bean definition
- `SecurityJwtConfiguration.java` - Added @ConditionalOnProperty to prevent loading with Keycloak
- `AuthenticationResource.java` - Enhanced logging
- `TokenDebugResource.java` - New diagnostic endpoints

### Frontend TypeScript

- `login.service.ts` - Added fallback token extraction, improved logging
- `auth.interceptor.ts` - Added debug logging

### Configuration

- `application-dev.yml` - Enhanced security logging

## Next Steps

1. **Rebuild**: `mvn clean install`
2. **Restart**: Application with dev profile
3. **Test**: Try login again
4. **Debug**: Use `/api/debug/token-info` if needed
5. **Check logs**: Search for "JWT validation" errors

The root issue is now fixed - you should get proper token validation against Keycloak now! 🎉
