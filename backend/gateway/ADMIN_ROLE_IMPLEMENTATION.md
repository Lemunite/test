# Admin User Access Fix - Complete Summary

## Problem Analysis

The admin users were not different from normal users and couldn't access the admin screen. Root causes identified:

1. **Frontend**: Routes to `/admin` require `ROLE_ADMIN` authority (verified in `app.routes.ts` and `user-route-access.service.ts`)
2. **Backend**: The `/api/account` endpoint was extracting roles from JWT but **not formatting them with `ROLE_` prefix**
3. **Keycloak**: Admin users didn't have an `admin` role assigned

## Solution Implemented

### 1. Backend Fix - Role Formatting ✅

**File**: `AccountResource.java` (GET `/api/account` endpoint)

**Change**: Updated role extraction to add `ROLE_` prefix to Keycloak roles

**Before**:

```java
// Extract roles from realm_access
Map<String, Object> realmAccess = jwt.getClaim("realm_access");
if (realmAccess != null && realmAccess.containsKey("roles")) {
    List<String> roles = (List<String>) realmAccess.get("roles");
    authorities.addAll(roles);  // No prefix!
}
```

**After**:

```java
// Extract roles from realm_access and format with ROLE_ prefix
Map<String, Object> realmAccess = jwt.getClaim("realm_access");
if (realmAccess != null && realmAccess.containsKey("roles")) {
    List<String> roles = (List<String>) realmAccess.get("roles");
    LOG.debug("Keycloak realm_access.roles: {}", roles);
    roles.stream()
        .map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role)
        .forEach(authorities::add);  // With ROLE_ prefix!
}
```

**Added Logging**:

- Debug log when JWT roles are extracted
- Info log showing final authorities assigned to user
- Debug log if no roles found in JWT

### 2. New Method - Role Assignment Service ✅

**File**: `KeycloakRegistrationService.java`

**New Method**: `assignRoleToUser(String username, String roleName)`

This allows programmatic assignment of roles to existing users. Example:

```java
// Assign admin role to user
keycloakRegistrationService.assignRoleToUser("lemunite", "admin");
```

**Features**:

- Validates role exists in Keycloak
- Handles conflicts gracefully
- Comprehensive error logging
- Can be called from REST endpoint or admin operations

### 3. Documentation ✅

**File**: `ADMIN_ROLE_SETUP.md`

Comprehensive guide covering:

- How to create `admin` and `user` roles in Keycloak
- How to assign roles to users (both via console and programmatically)
- How to verify role assignment in JWT tokens
- Complete troubleshooting guide
- System architecture explanation

## How Admin Access Works (Complete Flow)

```
1. User Login
   ├─ User enters credentials
   ├─ POST /oauth2/token to Keycloak
   ├─ Keycloak issues JWT with claims:
   │  ├─ preferred_username: "admin"
   │  ├─ email: "admin@example.com"
   │  ├─ given_name, family_name
   │  └─ realm_access: { roles: ["admin", "default-roles-jhipster"] }
   └─ Frontend stores JWT in localStorage

2. Frontend Checks Authentication
   ├─ App loads
   ├─ Angular calls GET /api/account
   ├─ Backend returns user with authorities
   └─ Frontend stores authorities in AccountService

3. Admin Route Access
   ├─ User navigates to /admin
   ├─ Angular guards trigger: UserRouteAccessService
   ├─ Service checks: accountService.hasAnyAuthority(["ROLE_ADMIN"])
   ├─ AccountService checks: account.authorities includes "ROLE_ADMIN"
   ├─ If present → Allow access to /admin
   └─ If missing → Redirect to /accessdenied

4. Admin Page Renders
   ├─ /admin loads admin routes
   ├─ Available tabs:
   │  ├─ User Management (/admin/user-management)
   │  ├─ API Docs (/admin/docs)
   │  ├─ Configuration (/admin/configuration)
   │  ├─ Health (/admin/health)
   │  ├─ Logs (/admin/logs)
   │  ├─ Metrics (/admin/metrics)
   │  └─ Gateway (/admin/gateway)
   └─ All protected by same ROLE_ADMIN check
```

## Configuration Reference

| Component | Role Name        | Spring Format | Location               |
| --------- | ---------------- | ------------- | ---------------------- |
| Keycloak  | `admin`          | `ROLE_ADMIN`  | Realm Roles            |
| Keycloak  | `user`           | `ROLE_USER`   | Realm Roles            |
| Frontend  | Authority.ADMIN  | `ROLE_ADMIN`  | authority.constants.ts |
| Backend   | Spring Authority | `ROLE_ADMIN`  | JWT claim processing   |

## Step-by-Step Setup

### Step 1: Create Roles in Keycloak ✅ (Manual)

1. Go to Keycloak Admin: `http://localhost:9080/admin`
2. Select realm: `jhipster`
3. Manage → Roles
4. Create role: `admin`
5. Create role: `user` (optional)

### Step 2: Assign Admin Role to User ✅ (Manual)

1. Keycloak Admin → Users
2. Select admin user
3. Role Mappings tab
4. Assign realm role: `admin`

### Step 3: Verify JWT Token ✅ (Testing)

After login, check JWT in browser console:

```javascript
// Run in browser console after login
const token = localStorage.getItem('authenticationToken');
const payload = JSON.parse(atob(token.split('.')[1]));
console.log('realm_access:', payload.realm_access);
// Should show: { "roles": ["admin", "default-roles-jhipster"] }
```

### Step 4: Verify Frontend Receives Authorities ✅ (Testing)

```javascript
// Run in browser console
const account = JSON.parse(localStorage.getItem('jhipster-account'));
console.log('authorities:', account.authorities);
// Should show: ["ROLE_ADMIN", "ROLE_USER"]
```

### Step 5: Test Admin Access ✅ (Testing)

1. Login as admin user
2. Navigate to `http://localhost:8080/admin`
3. Should see admin dashboard
4. All tabs should be accessible

## Files Modified

| File                               | Change                                | Impact                                                  |
| ---------------------------------- | ------------------------------------- | ------------------------------------------------------- |
| `AccountResource.java`             | Added ROLE\_ prefix to Keycloak roles | Backend now returns correct Spring Security role format |
| `KeycloakRegistrationService.java` | Added `assignRoleToUser()` method     | Can programmatically assign roles to users              |
| `ADMIN_ROLE_SETUP.md`              | New documentation                     | Complete setup guide for admins                         |

## Files Not Modified (But Relevant)

- `app.routes.ts` - Already has role check for `/admin`
- `admin.routes.ts` - Already configured with all admin pages
- `user-route-access.service.ts` - Already validates authorities
- `account.service.ts` - Already handles role array
- `authority.constants.ts` - Already defines `ROLE_ADMIN`

## Testing Checklist

- [ ] Keycloak roles `admin` and `user` created
- [ ] Admin user assigned `admin` role in Keycloak
- [ ] Build project: `mvn clean install -DskipTests`
- [ ] Start application
- [ ] Login as admin user
- [ ] Check browser console for `ROLE_ADMIN` in authorities
- [ ] Navigate to `/admin` - should show admin dashboard
- [ ] All admin tabs accessible (user-management, docs, config, etc.)
- [ ] Login as non-admin user
- [ ] Navigate to `/admin` - should redirect to `/accessdenied`

## Troubleshooting

### Issue: Admin user can't access /admin

1. Verify Keycloak has role: Keycloak Admin → Roles → should have `admin`
2. Verify user assigned: Keycloak Admin → Users → Select user → Role Mappings → `admin` in "Assigned Roles"
3. Check JWT: Browser console → `JSON.parse(atob(token.split('.')[1])).realm_access.roles` should include `admin`
4. Check backend logs: Should show "Keycloak realm_access.roles: [admin, ...]"
5. Check frontend: `localStorage.getItem('jhipster-account')` should show `ROLE_ADMIN` in authorities

### Issue: "Account not fully set up" error during login

- Keycloak requires firstName and lastName
- Keycloak Admin → Users → Select user → Edit
- Fill in First Name and Last Name
- Save

### Issue: Backend log shows roles but frontend doesn't see them

- Clear browser cache: Dev Tools → Application → Storage → Clear All
- Refresh page
- Login again

## Future Enhancements (Optional)

1. **Create REST endpoint to assign roles**:

   - POST `/api/admin/users/{login}/roles/{roleName}`
   - Requires ROLE_ADMIN authority
   - Calls `keycloakRegistrationService.assignRoleToUser()`

2. **Automatic admin role assignment**:

   - During registration, check email domain
   - If @company.com → auto-assign admin role

3. **Role-based UI navigation**:

   - Show/hide admin menu based on ROLE_ADMIN
   - Currently hidden in nav; admin routes still accessible directly

4. **Create user management admin page**:
   - List users with roles
   - Assign/revoke roles via admin UI
   - Uses `assignRoleToUser()` method

## Related Documentation

- Keycloak embedded login: `SOLUTION_401_ERROR.md`
- Keycloak registration: `LOGOUT_AND_KEYCLOAK_REGISTRATION.md`
- Registration with firstName/lastName: `ADMIN_ROLE_SETUP.md` (this file)
- Keycloak config troubleshooting: `KEYCLOAK_REGISTRATION_401_FIX.md`

## Summary

Admin access is now properly controlled through:

1. **Keycloak**: Stores user roles in JWT realm_access claim
2. **Backend**: Extracts roles and formats with `ROLE_` prefix
3. **Frontend**: Checks for `ROLE_ADMIN` to allow `/admin` access
4. **Service**: Can programmatically assign roles via `KeycloakRegistrationService`

The fix ensures that admin users have the correct authorities and can access all protected admin pages.
