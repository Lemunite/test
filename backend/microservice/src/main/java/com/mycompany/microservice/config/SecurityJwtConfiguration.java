package com.mycompany.microservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration for Keycloak Integration
 *
 * The microservice validates JWT tokens issued by Keycloak using the public JWK Set.
 * Spring Boot auto-configuration automatically configures the JWT decoder based on:
 * - spring.security.oauth2.resourceserver.jwt.issuer-uri
 * - spring.security.oauth2.resourceserver.jwt.jwk-set-uri
 *
 * These are configured in application.yml files.
 */
@Configuration
public class SecurityJwtConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityJwtConfiguration.class);
    // Spring Boot auto-configuration handles JWT decoder setup
    // No additional beans needed - configuration is in application.yml
}
