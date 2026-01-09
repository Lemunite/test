package com.mycompany.gateway.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Service for registering users directly in Keycloak
 * Used for embedded login flow user registration
 */
@Service
@ConditionalOnProperty(name = "keycloak.enabled", havingValue = "true")
public class KeycloakRegistrationService {

    private static final Logger log = LoggerFactory.getLogger(KeycloakRegistrationService.class);

    @Value("${keycloak.auth-server-url:}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:}")
    private String realm;

    @Value("${keycloak.resource:}")
    private String clientId;

    @Value("${keycloak.credentials.secret:}")
    private String clientSecret;

    private final RestTemplate restTemplate;

    public KeycloakRegistrationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Register a new user in Keycloak
     *
     * @param username the username (login)
     * @param email the email address
     * @param password the password
     * @param firstName the first name
     * @param lastName the last name
     * @throws KeycloakRegistrationException if registration fails
     */
    public void registerUser(String username, String email, String password, String firstName, String lastName)
        throws KeycloakRegistrationException {
        log.info("Attempting to register user in Keycloak: {}", username);

        try {
            // First, get admin token
            String adminToken = getAdminToken();

            // Then create the user in Keycloak
            createKeycloakUser(adminToken, username, email, firstName, lastName);

            // Set user password
            setUserPassword(adminToken, username, password);

            log.info("Successfully registered user in Keycloak: {}", username);
        } catch (Exception e) {
            log.error("Failed to register user in Keycloak: {}", username, e);
            throw new KeycloakRegistrationException("Registration failed: " + e.getMessage(), e);
        }
    }

    /**
     * Get an admin token for Keycloak API calls
     */
    private String getAdminToken() throws KeycloakRegistrationException {
        String tokenEndpoint = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakServerUrl, realm);

        log.debug("Requesting admin token from Keycloak at: {}", tokenEndpoint);
        log.debug("Using client_id: {} for client_credentials flow", clientId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(tokenEndpoint, new HttpEntity<>(body, headers), Map.class);

            if (response.getBody() != null && response.getBody().containsKey("access_token")) {
                log.debug("Successfully obtained admin token from Keycloak");
                return (String) response.getBody().get("access_token");
            }

            log.error("Keycloak token response missing access_token. Response: {}", response.getBody());
            throw new KeycloakRegistrationException("Failed to obtain admin token from Keycloak - no access_token in response");
        } catch (HttpClientErrorException e) {
            log.error("Failed to get admin token from Keycloak");
            log.error("Status: {}", e.getStatusCode());
            log.error("Message: {}", e.getMessage());
            log.error("Response body: {}", e.getResponseBodyAsString());
            log.error("Possible causes:");
            log.error("  1. Client credentials (client_id/client_secret) are incorrect");
            log.error("  2. Client '{}' does not have 'client_credentials' grant type enabled", clientId);
            log.error("  3. Client does not have service account role assignment");
            log.error("  4. Keycloak server is not accessible at: {}", keycloakServerUrl);

            throw new KeycloakRegistrationException(
                "Failed to get admin token: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(),
                e
            );
        } catch (Exception e) {
            log.error("Unexpected error while getting admin token: {}", e.getMessage(), e);
            throw new KeycloakRegistrationException("Failed to get admin token: " + e.getMessage(), e);
        }
    }

    /**
     * Create a user in Keycloak
     */
    private void createKeycloakUser(String adminToken, String username, String email, String firstName, String lastName)
        throws KeycloakRegistrationException {
        String usersEndpoint = String.format("%s/admin/realms/%s/users", keycloakServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> userRepresentation = new HashMap<>();
        userRepresentation.put("username", username);
        userRepresentation.put("email", email);
        userRepresentation.put("firstName", firstName != null ? firstName : "");
        userRepresentation.put("lastName", lastName != null ? lastName : "");
        userRepresentation.put("enabled", true);
        userRepresentation.put("emailVerified", false);
        userRepresentation.put("credentials", new java.util.ArrayList<>());

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                usersEndpoint,
                new HttpEntity<>(userRepresentation, headers),
                String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new KeycloakRegistrationException("Failed to create user in Keycloak: " + response.getStatusCode());
            }

            log.debug("User created successfully in Keycloak: {}", username);
        } catch (HttpClientErrorException e) {
            log.error("Failed to create user in Keycloak: HTTP {} - {}", e.getStatusCode(), e.getMessage());

            if (e.getStatusCode() == HttpStatus.CONFLICT) {
                throw new KeycloakRegistrationException("Username or email already exists");
            }

            throw new KeycloakRegistrationException("Failed to create user: " + e.getMessage(), e);
        }
    }

    /**
     * Set password for a user in Keycloak
     */
    private void setUserPassword(String adminToken, String username, String password) throws KeycloakRegistrationException {
        try {
            // First get the user ID
            String userId = getKeycloakUserId(adminToken, username);

            // Then set the password
            String resetPasswordEndpoint = String.format("%s/admin/realms/%s/users/%s/reset-password", keycloakServerUrl, realm, userId);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> credentialRepresentation = new HashMap<>();
            credentialRepresentation.put("type", "password");
            credentialRepresentation.put("value", password);
            credentialRepresentation.put("temporary", false);

            var response = restTemplate.exchange(
                resetPasswordEndpoint,
                HttpMethod.PUT,
                new HttpEntity<>(credentialRepresentation, headers),
                String.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to set password for user {}: HTTP {}", username, response.getStatusCode());
                throw new KeycloakRegistrationException("Failed to set password: " + response.getStatusCode());
            }

            log.debug("Password set successfully for user: {}", username);
        } catch (HttpClientErrorException e) {
            log.error("HTTP error setting password for user {}: {} - {}", username, e.getStatusCode(), e.getMessage());
            throw new KeycloakRegistrationException("Failed to set password: " + e.getStatusCode(), e);
        } catch (Exception e) {
            log.error("Failed to set password for user: {}", username, e);
            throw new KeycloakRegistrationException("Failed to set password: " + e.getMessage(), e);
        }
    }

    /**
     * Get Keycloak user ID by username
     */
    private String getKeycloakUserId(String adminToken, String username) throws KeycloakRegistrationException {
        String usersSearchEndpoint = String.format("%s/admin/realms/%s/users?username=%s", keycloakServerUrl, realm, username);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);

        try {
            ResponseEntity<Map[]> response = restTemplate.exchange(
                usersSearchEndpoint,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Map[].class
            );

            if (response.getBody() != null && response.getBody().length > 0) {
                return (String) response.getBody()[0].get("id");
            }

            throw new KeycloakRegistrationException("User not found in Keycloak: " + username);
        } catch (Exception e) {
            log.error("Failed to get user ID from Keycloak: {}", username, e);
            throw new KeycloakRegistrationException("Failed to get user ID: " + e.getMessage(), e);
        }
    }

    /**
     * Assign a realm role to a user in Keycloak
     *
     * @param username the username to assign role to
     * @param roleName the role name to assign (e.g., "admin", "user")
     * @throws KeycloakRegistrationException if assignment fails
     */
    public void assignRoleToUser(String username, String roleName) throws KeycloakRegistrationException {
        log.info("Attempting to assign role '{}' to user '{}' in Keycloak", roleName, username);

        try {
            String adminToken = getAdminToken();
            String userId = getKeycloakUserId(adminToken, username);

            // Get the role ID
            String rolesEndpoint = String.format("%s/admin/realms/%s/roles/%s", keycloakServerUrl, realm, roleName);

            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(adminToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> roleResponse = restTemplate.getForEntity(rolesEndpoint, Map.class);

            if (!roleResponse.getStatusCode().is2xxSuccessful() || roleResponse.getBody() == null) {
                throw new KeycloakRegistrationException("Role not found in Keycloak: " + roleName);
            }

            Map<String, Object> role = roleResponse.getBody();

            // Assign role to user
            String assignRoleEndpoint = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakServerUrl, realm, userId);

            List<Map<String, Object>> roleList = new ArrayList<>();
            roleList.add(role);

            ResponseEntity<String> assignResponse = restTemplate.exchange(
                assignRoleEndpoint,
                HttpMethod.POST,
                new HttpEntity<>(roleList, headers),
                String.class
            );

            if (!assignResponse.getStatusCode().is2xxSuccessful()) {
                log.error("Failed to assign role to user: HTTP {}", assignResponse.getStatusCode());
                throw new KeycloakRegistrationException("Failed to assign role: " + assignResponse.getStatusCode());
            }

            log.info("Successfully assigned role '{}' to user '{}'", roleName, username);
        } catch (KeycloakRegistrationException e) {
            throw e;
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("User or role not found in Keycloak");
            } else if (e.getStatusCode() == HttpStatus.CONFLICT) {
                log.warn("User already has role '{}', skipping assignment", roleName);
            } else {
                log.error("Failed to assign role: HTTP {} - {}", e.getStatusCode(), e.getMessage());
            }
            throw new KeycloakRegistrationException("Failed to assign role: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error assigning role: {}", e.getMessage(), e);
            throw new KeycloakRegistrationException("Failed to assign role: " + e.getMessage(), e);
        }
    }

    /**
     * Custom exception for Keycloak registration failures
     */
    public static class KeycloakRegistrationException extends Exception {

        public KeycloakRegistrationException(String message) {
            super(message);
        }

        public KeycloakRegistrationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
