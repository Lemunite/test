# Admin Role Setup for Keycloak Embedded Authentication

## Problem

Admin users cannot access the admin screen (`/admin`) even though the admin views exist. The issue is that:

1. **Frontend** requires users to have `ROLE_ADMIN` authority to access `/admin` routes
2. **Backend** extracts roles from Keycloak's JWT token (`realm_access.roles`)
3. **Missing Link**: Admin users don't have the `admin` role assigned in Keycloak

## Solution

### Step 1: Create an "admin" Role in Keycloak

1. Open Keycloak Admin Console: `http://localhost:9080/admin`
2. Login with admin credentials
3. Navigate to **Realm Settings** → **jhipster**
4. Go to **Roles** tab
5. Click **Create role**
6. Enter role name: `admin`
7. Click **Create**

### Step 2: Create an "user" Role in Keycloak (Optional)

Repeat Step 1 with role name: `user`

### Step 3: Assign Admin Role to Your Admin User

#### Option A: Via Keycloak Admin Console

1. Go to **Manage** → **Users**
2. Click on your admin user (e.g., "admin")
3. Go to **Role Mappings** tab
4. Under "Realm Roles", select `admin` from the dropdown
5. Click **Add selected**
6. Verify: `admin` appears in "Assigned Roles"

#### Option B: Via Backend - Create Admin User Creation Endpoint (Recommended for automation)

You can add a utility method to create admin users with roles in Keycloak using the `KeycloakRegistrationService`.

### Step 4: Verify Role Assignment in JWT Token

After logging in with admin user:

1. Go to `http://localhost:8080/api/debug/token-info` (if you have the debug endpoint)
2. OR check browser console and look at the JWT token in Storage
3. The JWT should contain:

```json
{
  "realm_access": {
    "roles": ["admin", "default-roles-jhipster"]
  }
}
```

### Step 5: Verify Frontend Access

1. Login with admin user
2. Navigate to `http://localhost:8080/admin`
3. You should now see the admin dashboard with tabs:
   - User Management
   - Docs
   - Configuration
   - Health
   - Logs
   - Metrics
   - Gateway

## How It Works

### Frontend Flow

```
/admin route (app.routes.ts)
  ├─ Guard: UserRouteAccessService
  │  └─ Checks: Authority.ADMIN = "ROLE_ADMIN"
  │     └─ Calls: AccountService.hasAnyAuthority(["ROLE_ADMIN"])
  │        └─ Checks: account.authorities includes "ROLE_ADMIN"
  └─ If authorized → Load admin routes
     └─ If not → Navigate to "/accessdenied"
```

### Backend Flow

```
GET /api/account (AccountResource.java)
  ├─ Extract JWT from Spring Security context
  ├─ Get username, email, firstName, lastName from JWT claims
  ├─ Extract roles from jwt.realm_access.roles
  ├─ Format each role with "ROLE_" prefix
  │  └─ "admin" → "ROLE_ADMIN"
  │  └─ "user" → "ROLE_USER"
  ├─ Add Spring Security authorities
  └─ Return AdminUserDTO with authorities
```

### Keycloak Configuration

| Item       | Value                                      | Notes                               |
| ---------- | ------------------------------------------ | ----------------------------------- |
| Realm      | `jhipster`                                 | Main realm for application          |
| Client     | `web_app`                                  | Public client for embedded login    |
| Grant Type | ROPC (Resource Owner Password Credentials) | Embedded login flow                 |
| Roles      | `admin`, `user`                            | Assign to users as needed           |
| Token      | JWT                                        | Contains `realm_access.roles` claim |

## Troubleshooting

### Admin user can't access /admin

**Check 1**: Verify JWT contains roles

```
Browser Dev Tools → Storage → Application → AuthToken
Look for: "realm_access": { "roles": [...] }
```

**Check 2**: Verify backend formats roles with "ROLE\_" prefix

```
GET /api/account response should show:
{
  "authorities": ["ROLE_ADMIN", "ROLE_USER"],
  ...
}
```

**Check 3**: Verify frontend receives correct authorities

```
Browser Console:
localStorage.getItem('jhipster-account')
Should contain "ROLE_ADMIN" in authorities array
```

**Check 4**: Keycloak Admin Console

- Users → Select user
- Role Mappings → Verify `admin` is in "Assigned Roles"
- (Not in "Available Roles")

### "Account Not Fully Set Up" Error (Related)

If user can login but can't access admin screen:

1. User might be missing `firstName` and `lastName` in Keycloak profile
2. Go to Keycloak Admin Console → Users → Select user
3. Edit user → Fill in "First Name" and "Last Name"
4. Save

### Can't Find Role Mappings Tab

If you don't see "Role Mappings" tab on user detail page:

1. Make sure you're in the correct realm (`jhipster`)
2. Make sure you're logged in as Keycloak admin
3. Try browser refresh

## Next Steps

1. **Assign admin role** to existing users in Keycloak
2. **Test admin access** by logging in and navigating to `/admin`
3. **Create automated admin user creation** if needed (optional backend feature)

## Code References

- Frontend route guard: [app.routes.ts](src/main/webapp/app/app.routes.ts#L18)
- Admin routes: [admin.routes.ts](src/main/webapp/app/admin/admin.routes.ts)
- Route access service: [user-route-access.service.ts](src/main/webapp/app/core/auth/user-route-access.service.ts)
- Authority constants: [authority.constants.ts](src/main/webapp/app/config/authority.constants.ts)
- Account service: [account.service.ts](src/main/webapp/app/core/auth/account.service.ts)
- Backend account endpoint: [AccountResource.java](src/main/java/com/mycompany/gateway/web/rest/AccountResource.java#L139)
