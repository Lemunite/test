package com.mycompany.gateway.web.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Controller to authenticate users using ROPC flow with Keycloak.
 */
@RestController
@RequestMapping("/api")
public class AuthenticationResource {

    private final Logger log = LoggerFactory.getLogger(AuthenticationResource.class);

    private final RestTemplate restTemplate;

    @Value("${keycloak.enabled:true}")
    private boolean keycloakEnabled;

    @Value("${keycloak.auth-server-url:}")
    private String keycloakServerUrl;

    @Value("${keycloak.realm:}")
    private String realm;

    @Value("${keycloak.resource:}")
    private String clientId;

    @Value("${keycloak.credentials.secret:}")
    private String clientSecret;

    public AuthenticationResource(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JWTToken> authenticate(@Valid @RequestBody LoginVM loginVM) {
        log.info("REST request to authenticate user: {}", loginVM.getUsername());

        if (keycloakEnabled) {
            return authenticateWithKeycloak(loginVM);
        } else {
            log.warn("Keycloak is not enabled");
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(new JWTToken("Keycloak is not enabled"));
        }
    }

    private ResponseEntity<JWTToken> authenticateWithKeycloak(LoginVM loginVM) {
        String tokenEndpoint = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakServerUrl, realm);

        log.debug("Authenticating with Keycloak at endpoint: {}", tokenEndpoint);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "password");
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }
        body.add("username", loginVM.getUsername());
        body.add("password", loginVM.getPassword());
        body.add("scope", "openid profile email");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            log.debug("Sending ROPC request to Keycloak for user: {}", loginVM.getUsername());
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.postForEntity(
                tokenEndpoint,
                request,
                KeycloakTokenResponse.class
            );

            if (response.getBody() != null) {
                String accessToken = response.getBody().getAccessToken();
                String refreshToken = response.getBody().getRefreshToken();

                log.info("Successfully obtained Keycloak token for user: {}", loginVM.getUsername());
                log.debug(
                    "Access token type: {}, expires in: {} seconds",
                    response.getBody().getTokenType(),
                    response.getBody().getExpiresIn()
                );

                // Create JWT token header
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Authorization", "Bearer " + accessToken);

                log.debug("Returning authentication response with Authorization header");

                return new ResponseEntity<>(new JWTToken(accessToken), responseHeaders, HttpStatus.OK);
            }

            log.warn("No token body returned from Keycloak for user: {}", loginVM.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (HttpClientErrorException e) {
            log.error("Authentication failed for user {}: HTTP {} - {}", loginVM.getUsername(), e.getStatusCode(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Error during authentication for user {}: {}", loginVM.getUsername(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/authenticate/refresh")
    public ResponseEntity<JWTToken> refreshToken(@RequestBody RefreshTokenVM refreshTokenVM) {
        log.debug("REST request to refresh token");

        if (!keycloakEnabled) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
        }

        String tokenEndpoint = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakServerUrl, realm);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientId);
        if (clientSecret != null && !clientSecret.isEmpty()) {
            body.add("client_secret", clientSecret);
        }
        body.add("refresh_token", refreshTokenVM.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<KeycloakTokenResponse> response = restTemplate.postForEntity(
                tokenEndpoint,
                request,
                KeycloakTokenResponse.class
            );

            if (response.getBody() != null) {
                String accessToken = response.getBody().getAccessToken();
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.add("Authorization", "Bearer " + accessToken);
                return new ResponseEntity<>(new JWTToken(accessToken), responseHeaders, HttpStatus.OK);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Error during token refresh: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    /**
     * Keycloak token response
     */
    static class KeycloakTokenResponse {

        @JsonProperty("access_token")
        private String accessToken;

        @JsonProperty("refresh_token")
        private String refreshToken;

        @JsonProperty("token_type")
        private String tokenType;

        @JsonProperty("expires_in")
        private Integer expiresIn;

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public Integer getExpiresIn() {
            return expiresIn;
        }

        public void setExpiresIn(Integer expiresIn) {
            this.expiresIn = expiresIn;
        }
    }

    /**
     * View Model object for storing the user's credentials.
     */
    static class LoginVM {

        @NotNull
        @Size(min = 1, max = 50)
        private String username;

        @NotNull
        @Size(min = 4, max = 100)
        private String password;

        private Boolean rememberMe;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Boolean getRememberMe() {
            return rememberMe;
        }

        public void setRememberMe(Boolean rememberMe) {
            this.rememberMe = rememberMe;
        }

        @Override
        public String toString() {
            return "LoginVM{" + "username='" + username + '\'' + ", rememberMe=" + rememberMe + '}';
        }
    }

    /**
     * View Model object for storing refresh token
     */
    static class RefreshTokenVM {

        private String refreshToken;

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
