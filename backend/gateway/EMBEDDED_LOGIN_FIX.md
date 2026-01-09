# Embedded Login Flow Fix - Analysis and Solution

## Problem Summary

The embedded (non-redirect) login flow with Keycloak was broken:

- ✅ Login succeeds and Keycloak returns a bearer token
- ✅ Backend sends the token back to frontend
- ❌ **Frontend stores the token but backend rejects subsequent requests**
- ❌ Page remains locked - user not authenticated after login

## Root Causes Identified

### 1. **Token Validation Chain Issue**

**File**: `src/main/java/com/mycompany/gateway/config/SecurityConfiguration.java`

The backend JWT decoder was not properly configured to validate Keycloak tokens when enabled:

```java
@Bean
public ReactiveJwtDecoder jwtDecoder() {
  String issuerUri = keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm();
  return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
}

```

**Issue**: No validation that Keycloak is actually enabled before attempting to configure the decoder.

**Fix**: Added guard to ensure Keycloak configuration:

```java
@Bean
public ReactiveJwtDecoder jwtDecoder() {
  if (keycloakProperties.isEnabled()) {
    String issuerUri = keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm();
    return ReactiveJwtDecoders.fromIssuerLocation(issuerUri);
  }
  throw new IllegalStateException("JWT Decoder not properly configured. Keycloak must be enabled.");
}

```

### 2. **Insufficient Token Extraction Logic**

**File**: `src/main/webapp/app/login/login.service.ts`

Frontend was only trying to extract token from the Authorization header:

```typescript
.pipe(
  tap(response => {
    const bearer = response.headers.get('Authorization');
    if (bearer) {
      const jwt = bearer.replace('Bearer ', '');
      this.storeAuthenticationToken(jwt);
    }
  }),
```

**Issue**: If header extraction fails for any reason, no fallback to body's `id_token`.

**Fix**: Added fallback mechanism with better debugging:

```typescript
.pipe(
  tap(response => {
    let token = '';
    const bearer = response.headers.get('Authorization');
    if (bearer) {
      token = bearer.replace('Bearer ', '');
    }
    // Fallback to body's id_token if header is not present
    else if (response.body?.id_token) {
      token = response.body.id_token;
    }

    if (token) {
      this.storeAuthenticationToken(token);
      console.debug('Token stored successfully from authentication endpoint');
    } else {
      console.warn('No token found in authentication response');
    }
  }),
```

### 3. **Inadequate Debugging and Logging**

Multiple points in the authentication flow were missing proper logging:

- Backend authentication endpoint didn't log token response details
- Frontend interceptor didn't log when tokens were being added
- Token converter didn't log extracted authorities

**Fixes Applied**:

#### Backend (AuthenticationResource.java):

- Changed to `log.info()` level for authentication attempts
- Added detailed logging of Keycloak endpoint and response details
- Log HTTP error status codes and token expiration info
- Added debugging for token response body presence

#### Frontend (auth.interceptor.ts):

- Added debug logging when tokens are injected into requests
- Warning logs when requests are sent without tokens
- Conditional logging to avoid console spam in production

#### Backend (SecurityConfiguration.java):

- Added logging to JWT conversion process
- Log extracted roles from Keycloak tokens
- Log number of authorities granted

## Configuration Overview

### Keycloak Configuration

**File**: `src/main/resources/config/application.yml` and `application-dev.yml`

```yaml
keycloak:
  enabled: true
  auth-server-url: http://localhost:9080
  realm: jhipster
  resource: web_app
  credentials:
    secret: Jl6l62hyD6lBvC78DHO9mq2cFkgVVOvZ
```

**Verified**:

- ✅ Keycloak is enabled
- ✅ Correct server URL and realm
- ✅ Client ID matches (`web_app`)
- ✅ Client secret is provided

### Authentication Flow

1. **Frontend** → POST `/api/authenticate` with username/password
2. **Backend** (AuthenticationResource) → ROPC flow with Keycloak
3. **Keycloak** → Returns access_token + refresh_token
4. **Backend** → Returns token in Authorization header + body
5. **Frontend** → Stores token in localStorage/sessionStorage
6. **Frontend** → AuthInterceptor adds `Authorization: Bearer {token}` to all requests
7. **Backend** → SecurityConfiguration validates token against Keycloak issuer

## Files Modified

### Backend

1. **SecurityConfiguration.java**

   - Added guard for Keycloak enabled check in jwtDecoder()
   - Added Logger and logging to KeycloakJwtAuthenticationConverter

2. **AuthenticationResource.java**
   - Improved logging throughout authentication flow
   - Better error handling with detailed messages
   - Added token response details to logs

### Frontend

1. **login.service.ts**

   - Added fallback token extraction from response body
   - Improved debug logging for token storage
   - Better type hints for response body

2. **auth.interceptor.ts**
   - Added debug logging when token is injected
   - Warning logs for missing tokens on API requests
   - Conditional logging to minimize production overhead

## Testing the Fix

### Test Cases

1. **Successful Login**

   ```
   - Navigate to login page
   - Enter valid Keycloak credentials
   - Check browser console for: "Token stored successfully from authentication endpoint"
   - Check network tab for Authorization header in subsequent requests
   ```

2. **Token Storage**

   ```
   - After login, check localStorage/sessionStorage
   - Key: "jhi-authenticationToken"
   - Value: Should be a valid JWT token starting with "eyJ"
   ```

3. **Authorized API Calls**

   ```
   - After successful login, navigate to a protected page (e.g., /api/account)
   - Check network requests include Authorization header
   - Backend logs should show token validation success
   ```

4. **Token Validation**
   ```
   - Enable DEBUG level logging for com.mycompany.gateway.config.SecurityConfiguration
   - Look for logs showing extracted roles and created JwtAuthenticationToken
   ```

### Debug Logging

To enable detailed debugging, set log levels in `application.yml`:

```yaml
logging:
  level:
    ROOT: DEBUG
    tech.jhipster: DEBUG
    com.mycompany.gateway: DEBUG
    com.mycompany.gateway.config.SecurityConfiguration: DEBUG
    com.mycompany.gateway.web.rest.AuthenticationResource: DEBUG
```

## Browser Console Checks

After login, the browser console should show:

- ✅ `[AuthInterceptor] Adding token to request: GET /api/account`
- ✅ `Token stored successfully from authentication endpoint`
- ✅ No warnings about missing tokens

## Related Architecture

The application uses:

- **Spring Boot 3.x** with WebFlux (reactive)
- **Spring Security 6.x** with OAuth2 Resource Server
- **Keycloak** as OAuth2 provider (ROPC flow)
- **Angular 19+** with JWT token-based auth
- **JHipster** scaffolded project structure

## Important Notes

1. **ROPC Flow Security**: The Resource Owner Password Credentials (ROPC) flow is considered less secure than standard OAuth2 redirect flows. It's best used for trusted first-party applications.

2. **Token Refresh**: The implementation includes refresh token support via `/api/authenticate/refresh` endpoint for handling expired tokens.

3. **CORS Configuration**: The SecurityConfiguration properly exposes the Authorization header in CORS responses, allowing the frontend to access it.

4. **Multiple Decoders**: The application has both JWT and OAuth2 resource server configurations. When Keycloak is enabled, the OAuth2 (Keycloak) JWT decoder takes precedence.

## Verification Checklist

- [ ] Keycloak server is running and accessible
- [ ] Keycloak realm, client, and credentials are correct
- [ ] Backend can successfully obtain tokens from Keycloak
- [ ] Token is returned in Authorization header
- [ ] Frontend stores token in localStorage/sessionStorage
- [ ] Subsequent API calls include Authorization header
- [ ] Backend validates token signature against Keycloak issuer
- [ ] User authorities are properly extracted from Keycloak token
- [ ] Frontend recognizes user as authenticated
- [ ] Protected pages are accessible after login

## Next Steps for Complete Resolution

If issues persist after applying these fixes:

1. **Check Keycloak Logs**

   - Verify ROPC requests are being received
   - Check for invalid client errors
   - Look for credential validation failures

2. **Check Backend Logs** (with DEBUG enabled)

   - Verify token is being returned from Keycloak
   - Check JWT decoder initialization
   - Look for token validation errors

3. **Check Network Tab**

   - Verify Authorization header is in request
   - Check token format is `Bearer {token}`
   - Verify token is the one from login response

4. **Token Inspection**
   - Use jwt.io to decode and inspect the token
   - Verify issuer matches Keycloak realm
   - Check token expiration time
   - Verify `realm_access.roles` are present
