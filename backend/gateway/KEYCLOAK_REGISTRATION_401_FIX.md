# Keycloak Registration - Client Credentials Issue

## Current Error

```
401 Unauthorized on POST request for "http://localhost:9080/realms/jhipster/protocol/openid-connect/token"
```

This means the client credentials authentication is failing when trying to get an admin token from Keycloak.

## Root Causes

The error indicates one of these issues:

### 1. **Client Credentials Grant Type Not Enabled** ⚠️ (Most Common)

The `web_app` client must have the `client_credentials` grant type enabled.

**Keycloak Configuration Steps:**

1. Go to Keycloak Admin Console: `http://localhost:9080/admin`
2. Select realm: `jhipster`
3. Navigate to **Clients** → Select `web_app`
4. Go to **Capability config** tab
5. Ensure **"Client Credentials"** is enabled (toggle to ON)
6. Click **Save**

### 2. **Client Secret Mismatch**

The secret in `application.yml` might not match the one in Keycloak.

**How to Fix:**

1. In Keycloak Admin Console → Clients → `web_app`
2. Go to **Credentials** tab
3. Copy the Client Secret
4. Update `application.yml` and `application-dev.yml`:
   ```yaml
   keycloak:
     credentials:
       secret: { paste-the-correct-secret-here }
   ```

### 3. **Service Account Not Configured**

The `web_app` client needs a service account with proper roles.

**Keycloak Configuration Steps:**

1. In Keycloak Admin Console → Clients → `web_app`
2. Go to **Settings** tab
3. Scroll down to **Access Type**
4. Select **confidential** (not public)
5. Enable **Service Accounts Enabled**
6. Click **Save**

Then assign roles:

1. Go to **Service Account Roles** tab
2. Click **Assign role**
3. Select realm roles: `realm-admin` or `admin`
4. Click **Assign**

Alternatively, create a dedicated client:

1. Create new client called `web_app_admin` or `registration-admin`
2. Set as **confidential**
3. Enable **Service Accounts**
4. Assign `realm-admin` role
5. Use those credentials in the registration service config

### 4. **Keycloak Server Not Reachable**

The backend cannot reach Keycloak.

**How to Check:**

From your gateway backend container/machine:

```bash
curl -v http://localhost:9080/realms/jhipster/protocol/openid-connect/token
```

Should return 400 (bad request - missing credentials), not connection error.

---

## Complete Keycloak Setup for Registration

### Option A: Enable on Existing Client (Simpler)

1. **Go to Keycloak Admin Console**

   - URL: `http://localhost:9080/admin`
   - Login with admin credentials

2. **Select realm: `jhipster`**

3. **Go to Clients → `web_app`**

4. **Settings tab:**

   - Access Type: **confidential** ✓
   - Service Accounts Enabled: **ON** ✓
   - Valid Redirect URIs: Add `http://localhost:8080/*`
   - Web Origins: Add `http://localhost:8080`

5. **Capability config tab:**

   - Client Credentials: **ON** ✓

6. **Service Account Roles tab:**

   - Add role: `realm-admin`

7. **Credentials tab:**

   - Copy the Client Secret
   - Update in `application.yml`:
     ```yaml
     keycloak:
       credentials:
         secret: { copied-secret }
     ```

8. **Save and restart application**

### Option B: Create Dedicated Admin Client (More Secure)

1. **Create new client:**

   - Go to Clients → Create
   - Client ID: `registration-service`
   - Access Type: **confidential**
   - Service Accounts Enabled: **ON**
   - Save

2. **Get credentials:**

   - Credentials tab → copy Client Secret

3. **Assign roles:**

   - Service Account Roles → Add `realm-admin`

4. **Update configuration:**
   - Don't change `web_app` client
   - Update only the Keycloak registration service config
   - Create separate config for admin client

---

## Quick Fix Checklist

- [ ] Keycloak Admin Console open: `http://localhost:9080/admin`
- [ ] In realm `jhipster`, client `web_app` selected
- [ ] Access Type is `confidential` (not public)
- [ ] Service Accounts Enabled: **ON**
- [ ] Client Credentials grant type: **ON** (in Capability config)
- [ ] Service Account Roles: contains `realm-admin` or `admin`
- [ ] Client Secret copied correctly to `application.yml`
- [ ] Backend can reach Keycloak: `curl http://localhost:9080/realms/jhipster`
- [ ] Application rebuilt and restarted: `mvn clean install`

---

## Testing the Fix

### Test 1: Verify Client Configuration

```bash
# Should return 400 (missing credentials), not 401 or connection error
curl -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials"

# Expected response contains: missing_parameter, invalid_client, etc.
# NOT: invalid_client_id or invalid_client_secret
```

### Test 2: Try with Credentials

```bash
curl -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials" \
  -d "client_id=web_app" \
  -d "client_secret=YOUR_SECRET_HERE"

# Expected response: {"access_token": "...", "expires_in": 300, ...}
# If you get 401: Secret is wrong
# If you get 400 invalid_grant: Grant type not enabled
```

### Test 3: Try Registration

1. Navigate to `http://localhost:8080/register`
2. Fill in registration form
3. Submit
4. Check application logs for success or detailed error

---

## Troubleshooting Logs

### Look for These in Application Logs:

**Success:**

```
DEBUG KeycloakRegistrationService - Requesting admin token from Keycloak at: http://localhost:9080/realms/jhipster/protocol/openid-connect/token
DEBUG KeycloakRegistrationService - Using client_id: web_app for client_credentials flow
DEBUG KeycloakRegistrationService - Successfully obtained admin token from Keycloak
INFO  AccountResource - User successfully registered in Keycloak: username
INFO  AccountResource - User registered in local database: username
```

**Client Credentials Grant Not Enabled:**

```
ERROR KeycloakRegistrationService - Failed to get admin token from Keycloak
ERROR KeycloakRegistrationService - Status: 400
ERROR KeycloakRegistrationService - Message: invalid_grant
ERROR KeycloakRegistrationService - Response body: {"error":"invalid_grant","error_description":"Client not allowed to request grant_type"}
ERROR KeycloakRegistrationService - Possible causes:
ERROR KeycloakRegistrationService -   2. Client 'web_app' does not have 'client_credentials' grant type enabled
```

**Wrong Secret:**

```
ERROR KeycloakRegistrationService - Failed to get admin token from Keycloak
ERROR KeycloakRegistrationService - Status: 401
ERROR KeycloakRegistrationService - Message: Unauthorized
ERROR KeycloakRegistrationService - Response body: {"error":"invalid_client","error_description":"Client authentication failed"}
```

**Keycloak Unreachable:**

```
ERROR KeycloakRegistrationService - Unexpected error while getting admin token: I/O error on POST request for "http://localhost:9080/..."
```

---

## Alternative: Disable Keycloak Registration (Temporary)

If you can't configure Keycloak yet, registration will still work locally:

```java
// Registration will:
// 1. Try Keycloak - FAIL (logs warning)
// 2. Continue with local database registration - SUCCESS
// 3. User can login normally

```

The system is now designed to handle Keycloak registration failures gracefully.

---

## Configuration Reference

Your current configuration:

```yaml
keycloak:
  enabled: true
  auth-server-url: http://localhost:9080
  realm: jhipster
  resource: web_app
  credentials:
    secret: Jl6l62hyD6lBvC78DHO9mq2cFkgVVOvZ
```

**Verify each value:**

- `auth-server-url`: Can you ping `http://localhost:9080/realms/jhipster`?
- `realm`: Exactly `jhipster`?
- `resource`: Client ID is `web_app`?
- `secret`: Matches Keycloak Credentials tab?

---

## After Fixing Configuration

1. **Rebuild Application:**

   ```bash
   mvn clean install -DskipTests
   ```

2. **Restart Application**

3. **Test Registration:**

   - Go to register page
   - Should succeed
   - User should appear in both Keycloak and local database

4. **Verify:**
   - Keycloak Admin Console → Users → should see new user
   - Database → user table → should see new user
   - Can login with new credentials

---

## Still Not Working?

1. **Check logs for exact error message** - copy the full stack trace
2. **Verify client credentials are set correctly:**
   ```bash
   # From machine running Keycloak
   curl -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
     -H "Content-Type: application/x-www-form-urlencoded" \
     -d "grant_type=client_credentials" \
     -d "client_id=web_app" \
     -d "client_secret=Jl6l62hyD6lBvC78DHO9mq2cFkgVVOvZ"
   ```
3. **If curl succeeds but app fails:** Could be network/container networking issue
4. **Enable DEBUG logging** in `application.yml`:
   ```yaml
   logging:
     level:
       org.springframework.web.client: DEBUG
       com.mycompany.gateway.service.KeycloakRegistrationService: DEBUG
   ```

---

## Summary

The 401 error means Keycloak rejected your client credentials. Most likely cause:

✅ **Client Credentials grant type is not enabled** on the `web_app` client

**Quick Fix:**

1. Open Keycloak Admin Console
2. Go to Clients → web_app → Capability config
3. Turn ON: "Client Credentials"
4. Save
5. Rebuild and restart app
6. Try registration again

The system now includes better logging to help debug this. Check application logs for "Possible causes:" section which will guide you to the fix.
