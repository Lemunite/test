# Quick Start: Keycloak ROPC Authentication

## 30-Second Setup

### 1. Start Keycloak (if not running)

```bash
docker run -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin \
  -p 9080:8080 keycloak/keycloak:24.0.0 start-dev
```

### 2. Create Keycloak Client

- Go to: http://localhost:9080
- Create realm: `jhipster`
- Create client: `microservice`
- Enable: "Direct access grants"
- Copy client secret

### 3. Update Configuration

Edit `application-dev.yml`:

```yaml
keycloak:
  credentials:
    secret: YOUR_CLIENT_SECRET_HERE
```

### 4. Run Microservice

```bash
mvn spring-boot:run
```

### 5. Test Authentication

```bash
# Get token
curl -X POST http://localhost:8081/api/authenticate \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin"}'

# Use token
curl -X GET http://localhost:8081/api/patients \
  -H "Authorization: Bearer <TOKEN>"
```

## API Reference

### Authenticate (ROPC)

```
POST /api/authenticate
Content-Type: application/json

{
  "username": "user@example.com",
  "password": "password123"
}

Response:
{
  "access_token": "...",
  "token_type": "Bearer"
}
```

### Access Protected Endpoints

```
GET /api/patients
Authorization: Bearer <access_token>
```

## Configuration

### Development

- File: `application-dev.yml`
- Keycloak URL: `http://localhost:9080`
- Realm: `jhipster`

### Production

- Use environment variables:
  - `KEYCLOAK_AUTH_SERVER_URL`
  - `KEYCLOAK_REALM`
  - `KEYCLOAK_CLIENT_ID`
  - `KEYCLOAK_CLIENT_SECRET`

## Troubleshooting

| Issue             | Solution                                            |
| ----------------- | --------------------------------------------------- |
| 401 Unauthorized  | Check username/password and client secret           |
| Invalid signature | Verify jwk-set-uri is accessible and issuer matches |
| CORS error        | Configure CORS in Keycloak or API Gateway           |
| Roles not working | Assign roles to users in Keycloak                   |

## Files Modified

- `pom.xml` - Added Keycloak dependency
- `src/main/java/config/SecurityConfiguration.java` - Updated role converter
- `src/main/java/config/SecurityJwtConfiguration.java` - Simplified for Keycloak
- `src/main/java/config/RestTemplateConfiguration.java` - NEW
- `src/main/java/security/KeycloakTokenProvider.java` - NEW
- `src/main/java/web/rest/AuthenticateController.java` - NEW
- `application-dev.yml` - Added Keycloak config
- `application-prod.yml` - Added Keycloak config with env vars

## Next Steps

1. Read `KEYCLOAK_ROPC_SETUP.md` for detailed setup
2. Read `KEYCLOAK_CLIENT_SETUP.md` for client configuration
3. Read `MIGRATION_SUMMARY.md` for complete migration details
4. Test ROPC flow
5. Update frontend/gateway to use new `/api/authenticate` endpoint

## Support

For issues or questions:

1. Check Keycloak server logs
2. Check microservice application logs
3. Verify network connectivity between services
4. Review Keycloak documentation: https://www.keycloak.org/documentation

## Environment Variables (Production)

```bash
# Keycloak Configuration
export KEYCLOAK_AUTH_SERVER_URL=https://keycloak.example.com
export KEYCLOAK_REALM=jhipster
export KEYCLOAK_CLIENT_ID=microservice
export KEYCLOAK_CLIENT_SECRET=your-secret-here

# Start application
java -jar microservice.jar
```
