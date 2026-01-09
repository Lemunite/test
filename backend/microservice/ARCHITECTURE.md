# Architecture Diagrams

## Request Flow: Frontend → Gateway → Microservice → Keycloak

```
USER BROWSER (localhost:9000)
    │
    ├─ Login with username/password
    │
    ▼
GATEWAY (Port 9000)
    ├─ Receives credentials
    │
    ├─ POST /realms/jhipster/protocol/openid-connect/token
    │  (grant_type=password)
    │
    └─► KEYCLOAK (Port 9080)
         │
         ├─ Validates credentials
         │
         ├─ Issues JWT token
         │  {
         │    "access_token": "eyJ...",
         │    "token_type": "Bearer",
         │    "realm_access": {
         │      "roles": ["USER", "ADMIN"]
         │    }
         │  }
         │
         ◄─ Returns token
    │
    ├─ Stores token in browser
    │
    ├─ Frontend makes API request:
    │  GET /api/patients
    │  Authorization: Bearer eyJ...
    │  Origin: http://localhost:9000
    │
    ├─ Gateway routes to microservice
    │
    ▼
MICROSERVICE (Port 8081)
    ├─ ✅ Checks CORS origin (http://localhost:9000)
    │
    ├─ ✅ Validates Authorization header exists
    │
    ├─ ✅ Extracts JWT token
    │
    ├─ ✅ Validates token signature using Keycloak JWK Set
    │
    ├─ ✅ Extracts user info from token
    │  - Subject (user ID)
    │  - Roles (from realm_access.roles)
    │
    ├─ ✅ Checks authorization rules
    │  - Endpoint requires authentication ✓
    │  - User has required role ✓
    │
    ├─ ✅ Returns protected resource
    │  [
    │    { "id": 1, "name": "John" },
    │    { "id": 2, "name": "Jane" }
    │  ]
    │
    ◄─ Response with CORS headers
    │
    ├─ Gateway returns to browser
    │
    ▼
BROWSER
    ├─ Renders patient data
    │
    └─ User sees application
```

## CORS Handshake: Browser → Gateway → Microservice

```
BROWSER                                  GATEWAY                               MICROSERVICE
(http://localhost:9000)                 (Port 9000)                          (Port 8081)
    │                                       │                                     │
    │ Preflight Request                     │                                     │
    │ OPTIONS /api/patients                 │                                     │
    │ Origin: http://localhost:9000         │                                     │
    │ Access-Control-Request-Method: GET    │                                     │
    ├─────────────────────────────────────►│                                     │
    │                                       │ OPTIONS /api/patients               │
    │                                       │ Origin: http://localhost:9000       │
    │                                       ├────────────────────────────────────►│
    │                                       │                                     │
    │                                       │       Preflight Response            │
    │                                       │ 200 OK                              │
    │                                       │ Access-Control-Allow-Origin:        │
    │                                       │   http://localhost:9000             │
    │                                       │ Access-Control-Allow-Methods:       │
    │                                       │   GET, POST, PUT, DELETE            │
    │                                       │ Access-Control-Allow-Headers: *     │
    │                                       │◄────────────────────────────────────┤
    │       Preflight Response              │                                     │
    │ 200 OK                                │                                     │
    │ Access-Control-Allow-*                │                                     │
    │◄─────────────────────────────────────┤                                     │
    │                                       │                                     │
    │ Actual Request                        │                                     │
    │ GET /api/patients                     │                                     │
    │ Authorization: Bearer eyJ...          │                                     │
    │ Origin: http://localhost:9000         │                                     │
    ├─────────────────────────────────────►│                                     │
    │                                       │ GET /api/patients                   │
    │                                       │ Authorization: Bearer eyJ...        │
    │                                       │ Origin: http://localhost:9000       │
    │                                       ├────────────────────────────────────►│
    │                                       │                                     │
    │                                       │         Actual Response             │
    │                                       │ 200 OK                              │
    │                                       │ Access-Control-Allow-Origin:        │
    │                                       │   http://localhost:9000             │
    │                                       │ Access-Control-Allow-Credentials:   │
    │                                       │   true                              │
    │                                       │ Content: [patient data]             │
    │                                       │◄────────────────────────────────────┤
    │       Actual Response                 │                                     │
    │ 200 OK                                │                                     │
    │ Content: [patient data]               │                                     │
    │◄─────────────────────────────────────┤                                     │
    │                                       │                                     │
    ▼                                       ▼                                     ▼
Browser renders data              Gateway forwards response         Microservice continues
```

## Token Lifecycle

```
┌─────────────────────────────────────────────────────────────────┐
│ TOKEN LIFECYCLE                                                  │
└─────────────────────────────────────────────────────────────────┘

1. TOKEN CREATION
   ┌──────────────────────┐
   │ KEYCLOAK             │
   │                      │
   │ Password validation  │
   │ User lookup          │
   │ Role assignment      │
   │ Token encoding       │
   │ Token signing        │
   └──────────────────────┘
            │
            ▼ JWT Token
   eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.
   eyJqdGkiOiI4ZjQzZGRhYi1jMjQxLTQzZWEtODg5MC...
   AkGFhNwUwxW_9Fsi9Bb_Y

2. TOKEN TRANSPORT
   ┌──────────────────────┐
   │ GATEWAY              │
   │                      │
   │ Receives token       │
   │ Stores in browser    │
   │ Adds to requests     │
   └──────────────────────┘
            │
            ▼ Authorization Header
   Authorization: Bearer eyJhbGciOiJSUzI1NiI...

3. TOKEN VALIDATION
   ┌──────────────────────┐
   │ MICROSERVICE         │
   │                      │
   │ 1. Extract token     │
   │ 2. Verify signature  │
   │    (using Keycloak   │
   │     public key)      │
   │ 3. Check expiration  │
   │ 4. Extract claims    │
   │ 5. Map roles         │
   └──────────────────────┘
            │
            ▼
   ✅ Token Valid
   │
   ├─ user_id (subject)
   ├─ username
   ├─ email
   ├─ roles: [ADMIN, USER]
   └─ exp: 1673091600 (expiration)

4. TOKEN EXPIRATION
   ┌──────────────────────┐
   │ TIME PASSES          │
   │ exp: 1673091600      │ ← Current time passes this
   │ now: 1673091601      │
   └──────────────────────┘
            │
            ▼
   ❌ Token Expired
   │
   └─ Client gets 401
      └─ Refresh using refresh_token
```

## Authentication Decision Tree

```
                         Request arrives
                              │
                              ▼
                    ┌──────────────────┐
                    │ Is it a public   │
                    │ endpoint?        │
                    │ /management/     │
                    │   health         │
                    └────────┬─────────┘
                      Yes│  │No
                         │  │
                    ┌────▼──▼─────────┐
                    │ Allow & Return  │
                    └─────────────────┘

                                     ┌──────────────────────┐
                                     │ Check for JWT token  │
                                     │ Authorization header │
                                     └────────┬─────────────┘
                                        │    │
                              Has│  │Doesn't have
                                 │    │
                    ┌────────────▼┐   │
                    │ Return 401  │   │
                    │ Unauthorized│   │
                    └─────────────┘   │
                                      │
                                ┌─────▼──────────────┐
                                │ Validate token     │
                                │ - Signature       │
                                │ - Expiration      │
                                │ - Issuer          │
                                └────────┬──────────┘
                                    │    │
                           Valid│  │Invalid
                                 │    │
                    ┌────────────▼┐   │
                    │ Return 401  │   │
                    │ Unauthorized│   │
                    └─────────────┘   │
                                      │
                                ┌─────▼──────────────┐
                                │ Extract roles from │
                                │ token claims       │
                                └────────┬──────────┘
                                    │
                                ┌───▼──────────────────┐
                                │ Check authorization │
                                │ rules               │
                                │ - Admin role?       │
                                │ - User role?        │
                                └────────┬─────────────┘
                                    │    │
                         Authorized│  │Not authorized
                                 │    │
                    ┌────────────▼┐   │
                    │ Return 403  │   │
                    │ Forbidden   │   │
                    └─────────────┘   │
                                      │
                            ┌─────────▼──────────┐
                            │ Process request    │
                            │ Return 200 + data  │
                            └────────────────────┘
```

## Microservice Security Chain

```
┌────────────────────────────────────────────────────────────┐
│ SPRING SECURITY FILTER CHAIN                               │
└────────────────────────────────────────────────────────────┘

Request enters microservice
    │
    ▼ Filter 1
┌────────────────────────┐
│ CORS Filter            │
├────────────────────────┤
│ Check if preflight     │
│ Check origin allowed   │
│ Add CORS headers       │
└────────┬───────────────┘
         │
         ▼ Filter 2
┌────────────────────────┐
│ CSRF Filter            │
├────────────────────────┤
│ Disabled (stateless)   │
└────────┬───────────────┘
         │
         ▼ Filter 3
┌────────────────────────┐
│ Bearer Token Filter    │
├────────────────────────┤
│ Extract JWT token      │
│ Validate signature     │
│ Create Authentication  │
└────────┬───────────────┘
         │
         ▼ Filter 4
┌────────────────────────┐
│ Authorization Filter   │
├────────────────────────┤
│ Check endpoint rules   │
│ Match path patterns    │
│ Verify user role       │
└────────┬───────────────┘
         │
         ├─ ✅ Authorized
         │  │
         │  ▼ Handler
         │  Execute endpoint
         │  Return 200 + data
         │
         └─ ❌ Not Authorized
            │
            ▼ Exception Handler
            Return 401 or 403
```

## Keycloak JWT Token Structure

```
eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.
eyJqdGkiOiI4ZjQzZGRhYi1jMjQxLTQzZWEtODg5MC1kYjQxYTUyOWMwMTAiLCJleHAi
OjE2NzMwOTE2MDAsIm5iZiI6MCwiaWF0IjoxNjczMDkxMzAwLCJpc3MiOiJodHRwOi8v
bG9jYWxob3N0OjkwODAvcmVhbG1zL2poaXBzdGVyIiwiYXVkIjoiYWNjb3VudCIsInN1
YiI6ImI1ZjQ3YTI0LWU0YzItNDY1Ni1hY2E2LTdlOTMyMDI0YjEyZCIsInR5cCI6IkJl
YXJlciIsImF6cCI6Im1pY3Jvc2VydmljZSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6
WyJ1c2VyIiwiYWRtaW4iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJv
bGVzIjpbIm1hbmFnZS1hY2NvdW50Iiwiby1wcm9maWxlIiwibWFuYWdlLWFjY291bnQt
bGlua3MiXX19LCJuYW1lIjoiQWRtaW4gVXNlciIsInByZWZlcnJlZF91c2VybmFtZSI6
ImFkbWluIiwiZ2l2ZW5fbmFtZSI6IkFkbWluIiwiZmFtaWx5X25hbWUiOiJVc2VyIiwi
ZW1haWwiOiJhZG1pbkBsb2NhbGhvc3QifQ.
signature

DECODED:
┌──────────────────────────────────────┐
│ HEADER                               │
├──────────────────────────────────────┤
│ {                                    │
│   "alg": "RS256",                    │ ← Signing algorithm
│   "typ": "JWT"                       │ ← Token type
│ }                                    │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│ PAYLOAD (Claims)                     │
├──────────────────────────────────────┤
│ {                                    │
│   "jti": "8f43ddab-...",            │ ← Token ID
│   "exp": 1673091600,                 │ ← Expiration
│   "iat": 1673091300,                 │ ← Issued at
│   "iss": "http://localhost:9080/...",│ ← Issuer
│   "sub": "b5f47a24-...",            │ ← User ID
│   "username": "admin",               │ ← Username
│   "email": "admin@localhost",        │ ← Email
│   "realm_access": {                  │ ← Realm-level roles
│     "roles": ["user", "admin"]       │
│   },                                 │
│   "resource_access": {               │ ← Client-specific roles
│     "microservice": {                │
│       "roles": ["DOCTOR"]            │
│     }                                │
│   }                                  │
│ }                                    │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│ SIGNATURE                            │
├──────────────────────────────────────┤
│ RS256(base64(header) + "." +         │
│       base64(payload),               │
│       private_key)                   │ ← Signed by Keycloak private key
│                                      │ ← Verified using Keycloak public key
└──────────────────────────────────────┘
```

## Success Scenario: Complete Flow

```
Timeline: T0 → T5

T0: User opens frontend
    Browser: http://localhost:9000

T1: User logs in with credentials
    Frontend → Gateway
    POST /api/authenticate
    {"username": "admin", "password": "admin"}

T2: Gateway exchanges credentials with Keycloak
    Gateway → Keycloak
    POST /realms/jhipster/protocol/openid-connect/token
    grant_type=password&username=admin&password=admin

T3: Keycloak returns JWT token
    Keycloak → Gateway
    {
      "access_token": "eyJ...",
      "token_type": "Bearer",
      "expires_in": 300
    }

T4: Frontend makes API request with token
    Browser → Gateway
    GET /api/patients
    Authorization: Bearer eyJ...
    Origin: http://localhost:9000

T5: Gateway routes to Microservice
    Gateway → Microservice
    GET /api/patients
    Authorization: Bearer eyJ...

T6: Microservice validates token
    Microservice checks:
    ✅ Token signature (using Keycloak public key)
    ✅ Token expiration (not expired)
    ✅ Token issuer (matches Keycloak)
    ✅ CORS origin (http://localhost:9000 allowed)
    ✅ Authorization (user has required role)

T7: Microservice returns data
    Microservice → Gateway
    200 OK
    [
      {"id": 1, "name": "John"},
      {"id": 2, "name": "Jane"}
    ]
    Access-Control-Allow-Origin: http://localhost:9000

T8: Gateway returns data to frontend
    Gateway → Browser
    200 OK
    [patient data]

T9: Browser renders data
    Frontend displays patient list
    User sees the application working ✅
```

This completes the integration!
