# Keycloak Client Configuration Example

This file contains example configuration steps for setting up Keycloak to work with the ROPC microservice.

## Keycloak Realm and Client Setup

### Step 1: Create a Realm (if not exists)

1. Log in to Keycloak Admin Console (e.g., http://localhost:9080)
2. Click "Master" dropdown in top-left
3. Click "Create Realm"
4. Set realm name to: `jhipster`
5. Click "Create"

### Step 2: Create a Client

1. Navigate to: Realm Settings → Clients
2. Click "Create"
3. Set Client ID: `microservice`
4. Choose "OpenID Connect" as Client Type
5. Click "Next"
6. Configure capabilities:
   - **Capability config:**
     - ✅ Standard flow enabled
     - ✅ Direct access grants enabled (REQUIRED for ROPC)
     - ✅ Implicit flow enabled
7. Click "Save"

### Step 3: Get Client Credentials

1. Go to Client: microservice → Credentials tab
2. Regenerate secret and copy it
3. Use this in `KEYCLOAK_CLIENT_SECRET` environment variable

```
Example: a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6
```

### Step 4: Configure Access Settings

1. Go to Client: microservice → Settings tab
2. Set "Valid redirect URIs":
   ```
   http://localhost:8081/*
   http://localhost:3000/*
   https://your-domain.com/*
   ```
3. Set "Web Origins":
   ```
   +
   ```

### Step 5: Configure Token Settings

1. Go to Client: microservice → Advanced tab
2. Set "Access Token Lifespan": 300 (5 minutes) or desired value
3. Set "Refresh Token Lifespan": 1800 (30 minutes) or desired value
4. Click "Save"

## Create Roles

### Realm Roles

1. Navigate to: Realm Settings → Roles
2. Click "Create role"
3. Create the following roles:
   - `ADMIN`
   - `USER`
   - `DOCTOR`
   - `NURSE`

### Client Roles (Optional)

If you want client-specific roles:

1. Go to Client: microservice → Roles tab
2. Click "Create role"
3. Create client-specific roles as needed

## Create Users

### Create Admin User

1. Navigate to: User Management → Users
2. Click "Create user"
3. Set Username: `admin`
4. Set Email: `admin@localhost`
5. Click "Create"
6. Go to Credentials tab:
   - Set password
   - Turn OFF "Temporary"
7. Go to Role mapping tab:
   - Assign role: ADMIN

### Create Regular User

1. Click "Create user"
2. Set Username: `user`
3. Set Email: `user@localhost`
4. Click "Create"
5. Set password (not temporary)
6. Assign role: USER

## Test Configuration

### Using curl

```bash
# Get token for admin
curl -X POST http://localhost:9080/realms/jhipster/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password&client_id=microservice&client_secret=<YOUR_SECRET>&username=admin&password=<PASSWORD>"

# Response:
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "expires_in": 300,
  "refresh_expires_in": 1800,
  "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "token_type": "Bearer",
  "not-before-policy": 0,
  "session_state": "...",
  "scope": "..."
}
```

### Using Microservice Endpoint

```bash
# Get token from microservice
curl -X POST http://localhost:8081/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"<PASSWORD>"}'

# Response:
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI...",
  "token_type": "Bearer"
}
```

### Access Protected Resources

```bash
# Use token to access API
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <ACCESS_TOKEN>"
```

## Docker Compose Example

If running Keycloak in Docker:

```yaml
version: '3.8'

services:
  keycloak:
    image: keycloak/keycloak:24.0.0
    environment:
      KEYCLOAK_ADMIN: admin
      KEYCLOAK_ADMIN_PASSWORD: admin
    ports:
      - '9080:8080'
    command:
      - start-dev
    volumes:
      - keycloak_data:/opt/keycloak/data

volumes:
  keycloak_data:
```

Run with: `docker-compose up -d keycloak`

## Common Issues

### "Unsupported grant_type" or "Direct access grants not enabled"

- **Fix:** Enable "Direct access grants" on Client → Settings
- Go to Client: microservice → Settings → toggle "Direct access grants enabled"

### "Invalid client credentials"

- **Fix:** Verify client secret is correct
- Regenerate if needed from Client → Credentials

### Token validation fails in microservice

- **Fix:** Ensure Keycloak URL and realm name match configuration
- Check `spring.security.oauth2.resourceserver.jwt.jwk-set-uri`
- Verify Keycloak is accessible from microservice

### CORS errors when calling /api/authenticate from browser

- **Fix:** Configure CORS in Keycloak or API Gateway
- Add allowed origins to Client settings
- Or use API Gateway as proxy

## Postman Collection Example

```json
{
  "info": {
    "name": "Microservice ROPC Flow",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Authenticate",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": ["var jsonData = pm.response.json();", "pm.environment.set(\"access_token\", jsonData.access_token);"]
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [
          {
            "key": "Content-Type",
            "value": "application/json"
          }
        ],
        "body": {
          "mode": "raw",
          "raw": "{\"username\":\"admin\",\"password\":\"admin\"}"
        },
        "url": {
          "raw": "http://localhost:8081/api/authenticate",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "authenticate"]
        }
      }
    },
    {
      "name": "Get Patients",
      "request": {
        "method": "GET",
        "header": [
          {
            "key": "Authorization",
            "value": "Bearer {{access_token}}"
          }
        ],
        "url": {
          "raw": "http://localhost:8081/api/patients",
          "protocol": "http",
          "host": ["localhost"],
          "port": "8081",
          "path": ["api", "patients"]
        }
      }
    }
  ]
}
```

## Production Checklist

- [ ] Keycloak configured with production database
- [ ] TLS/HTTPS enabled for Keycloak
- [ ] Client secret stored securely (environment variable)
- [ ] Realm and roles properly configured
- [ ] Users assigned appropriate roles
- [ ] Token lifespan configured appropriately
- [ ] CORS settings configured for production domain
- [ ] Firewall rules configured for Keycloak access
- [ ] Backup and recovery procedures documented
- [ ] Monitoring and logging enabled
