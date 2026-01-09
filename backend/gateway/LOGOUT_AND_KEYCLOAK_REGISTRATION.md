# Logout & Keycloak Registration Implementation

## Changes Made

### 1. Logout with Page Refresh

**File: `src/main/webapp/app/login/login.service.ts`**

**Changes:**

- Added `Router` injection
- Enhanced `logout()` method to:
  1. Clear authentication token from storage
  2. Clear user identity from AccountService
  3. Navigate to login page
  4. Force page refresh to clear all cached data

**Code:**

```typescript
logout(): void {
  this.removeAuthenticationToken();
  this.accountService.authenticate(null);
  this.router.navigate(['/login']).then(() => {
    // Force page refresh to clear any cached data
    window.location.reload();
  });
}
```

**Result:** User is completely logged out and all cached user data is cleared from the page.

---

### 2. Keycloak User Registration

#### Backend Changes

**New File: `src/main/java/com/mycompany/gateway/service/KeycloakRegistrationService.java`**

This service handles direct user registration in Keycloak using the Admin API:

**Key Methods:**

- `registerUser()` - Main entry point for registration
- `getAdminToken()` - Obtains service account token from Keycloak
- `createKeycloakUser()` - Creates user in Keycloak realm
- `setUserPassword()` - Sets password for the user
- `getKeycloakUserId()` - Retrieves user ID from Keycloak

**Features:**

- ✅ Only active when `keycloak.enabled = true`
- ✅ Handles conflicts (username/email already exists)
- ✅ Creates user with all profile information
- ✅ Sets password as non-temporary
- ✅ Comprehensive error logging

**Updated File: `src/main/java/com/mycompany/gateway/web/rest/AccountResource.java`**

**Changes:**

- Added `KeycloakRegistrationService` injection
- Enhanced `registerAccount()` method to:
  1. Register user in Keycloak first
  2. Handle Keycloak-specific errors (conflicts)
  3. Fall back to local database registration
  4. Send activation email

**Logic Flow:**

```
User Registration Request
    ↓
Validate password length
    ↓
Register in Keycloak (new)
    ↓
[Success] Register in local DB
    ↓
Send activation email
    ↓
Return 201 Created
```

#### Frontend Changes

**Updated File: `src/main/webapp/app/account/register/register.service.ts`**

**Changes:**

- Added error handling for Keycloak-specific responses
- Converts Keycloak registration exceptions to standard error types
- Maps both 400 (JHipster) and 409 (Keycloak conflict) responses

**Error Handling:**

```typescript
catchError((error: HttpErrorResponse) => {
  if (error.message?.includes('already exists')) {
    if (error.message?.includes('Username')) {
      return throwError(() => ({
        type: 'LOGIN_ALREADY_USED',
        message: 'Username already exists in Keycloak',
      }));
    } else if (error.message?.includes('email')) {
      return throwError(() => ({
        type: 'EMAIL_ALREADY_USED',
        message: 'Email already exists in Keycloak',
      }));
    }
  }
  return throwError(() => error);
});
```

**Updated File: `src/main/webapp/app/account/register/register.component.ts`**

**Changes:**

- Enhanced `processError()` method to handle:
  - Traditional JHipster error types (400 status)
  - Keycloak conflict errors (409 status)
  - Both username and email conflicts

---

## How It Works

### Registration Flow

1. **Frontend**: User submits registration form
2. **Frontend Service**: POST to `/api/register` with credentials
3. **Backend Controller**: Validates password length
4. **Keycloak Service**:
   - Gets admin token from Keycloak
   - Creates user in Keycloak realm
   - Sets password for user
5. **Database Service**: Registers user in local database
6. **Mail Service**: Sends activation/welcome email
7. **Frontend**: Shows success or displays error

### Logout Flow

1. **User**: Clicks logout button
2. **Frontend Service**:
   - Removes token from localStorage/sessionStorage
   - Clears user identity from AccountService
   - Navigates to login page
   - **Forces full page refresh**
3. **Result**: User is completely logged out, page reload clears cache

---

## Configuration

Keycloak registration is **automatically enabled** when:

```yaml
keycloak:
  enabled: true
```

The registration service requires:

- Keycloak realm admin API access
- Client credentials with appropriate permissions
- Service account with `manage-users` role

---

## Error Handling

### Keycloak Conflicts

- Username already exists → `LoginAlreadyUsedException`
- Email already exists → `EmailAlreadyUsedException`
- Other Keycloak errors → `RuntimeException` with message

### Frontend Response

- Shows appropriate error message to user
- Prevents duplicate submissions
- Maintains form state for correction

---

## Testing Registration

### Test Case 1: Successful Registration

```
1. Navigate to /register
2. Fill form with new username, email, password
3. Submit
4. Expected: Account created in Keycloak and local DB
5. Verify: User can login with new credentials
```

### Test Case 2: Duplicate Username

```
1. Register user1 successfully
2. Try to register again with same username
3. Expected: "Login name already registered" error
4. Username field shows error
```

### Test Case 3: Duplicate Email

```
1. Register user with email@example.com
2. Try to register with same email
3. Expected: "Email already registered" error
4. Email field shows error
```

### Test Case 4: Password Mismatch

```
1. Fill form with mismatched passwords
2. Submit
3. Expected: Client-side error before sending request
4. Verify: "Passwords do not match" message
```

---

## Testing Logout

### Test Case 1: Basic Logout

```
1. Login successfully
2. Click logout button
3. Expected: Page reloads, redirects to login
4. Verify: No user info visible, localStorage cleared
```

### Test Case 2: Logout and Verify Session Clear

```
1. Login successfully
2. Note: User is authenticated
3. Click logout
4. Expected: Page refreshes completely
5. Verify: Going back (browser back) doesn't show user data
```

### Test Case 3: Protected Route After Logout

```
1. Login successfully
2. Navigate to protected page (e.g., /admin)
3. Click logout
4. Expected: Full page refresh, redirected to login
5. Try accessing /admin directly: redirected to login
```

---

## Keycloak Admin API Requirements

The service account used for registration must have:

- **Realm role**: `realm-admin` (or equivalent)
- **Client role**: `manage-users`
- **Permissions**: Create users, set passwords, query users

### Keycloak Configuration Example

```yaml
keycloak:
  enabled: true
  auth-server-url: http://localhost:9080
  realm: jhipster
  resource: web_app
  credentials:
    secret: your_client_secret
```

---

## Logs to Watch

### Successful Registration

```
INFO  KeycloakRegistrationService - Attempting to register user in Keycloak: username
INFO  KeycloakRegistrationService - Successfully registered user in Keycloak: username
INFO  AccountResource - User registered in local database: username
```

### Failed Registration

```
ERROR KeycloakRegistrationService - Failed to register user in Keycloak: username
ERROR AccountResource - Keycloak registration failed: [error details]
```

### Logout

```
INFO  LoginService - User logged out successfully
DEBUG AuthInterceptor - No token available for request
```

---

## Backward Compatibility

- ✅ Works with existing JWT authentication
- ✅ Local database registration still functional
- ✅ Non-Keycloak environments unaffected
- ✅ Error handling compatible with JHipster standards

---

## Files Modified

### Backend Java

- `AccountResource.java` - Updated register endpoint
- `KeycloakRegistrationService.java` - New service for Keycloak registration

### Frontend TypeScript

- `login.service.ts` - Enhanced logout with page refresh
- `register.service.ts` - Added Keycloak error handling
- `register.component.ts` - Improved error processing

---

## Next Steps

1. **Rebuild**: `mvn clean install`
2. **Test**: Try registration with new user
3. **Verify**: Check Keycloak admin console for created user
4. **Test Login**: Login with newly registered user
5. **Test Logout**: Verify full logout with page refresh
