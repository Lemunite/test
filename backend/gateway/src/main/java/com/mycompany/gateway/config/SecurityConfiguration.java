package com.mycompany.gateway.config;

import static org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers.pathMatchers;

import com.mycompany.gateway.security.AuthoritiesConstants;
import java.util.*;
import java.util.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.ReferrerPolicyServerHttpHeadersWriter;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.OrServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfiguration.class);

    private final JHipsterProperties jHipsterProperties;
    private final KeycloakProperties keycloakProperties;

    public SecurityConfiguration(JHipsterProperties jHipsterProperties, KeycloakProperties keycloakProperties) {
        this.jHipsterProperties = jHipsterProperties;
        this.keycloakProperties = keycloakProperties;
        log.info("SecurityConfiguration initialized. Keycloak enabled: {}", keycloakProperties.isEnabled());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .securityMatcher(
                new NegatedServerWebExchangeMatcher(
                    new OrServerWebExchangeMatcher(
                        pathMatchers(
                            "/app/**",
                            "/i18n/**",
                            "/content/**",
                            "/swagger-ui/**",
                            "/swagger-resources/**",
                            "/v2/api-docs",
                            "/v3/api-docs",
                            "/test/**"
                        ),
                        pathMatchers(HttpMethod.OPTIONS, "/**")
                    )
                )
            )
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .headers(headers ->
                headers
                    .contentSecurityPolicy(csp -> csp.policyDirectives(jHipsterProperties.getSecurity().getContentSecurityPolicy()))
                    .frameOptions(frameOptions -> frameOptions.disable())
                    .referrerPolicy(referrer ->
                        referrer.policy(ReferrerPolicyServerHttpHeadersWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                    )
                    .permissionsPolicy(permissions ->
                        permissions.policy(
                            "camera=(), fullscreen=(self), geolocation=(), gyroscope=(), magnetometer=(), microphone=(), midi=(), payment=(), sync-xhr=()"
                        )
                    )
            )
            .requestCache(cache -> cache.requestCache(NoOpServerRequestCache.getInstance()))
            .authorizeExchange(exchanges ->
                exchanges
                    .pathMatchers("/")
                    .permitAll()
                    .pathMatchers("/*.*")
                    .permitAll()
                    .pathMatchers("/api/authenticate")
                    .permitAll()
                    .pathMatchers("/api/authenticate/refresh")
                    .permitAll()
                    .pathMatchers("/api/register")
                    .permitAll()
                    .pathMatchers("/api/activate")
                    .permitAll()
                    .pathMatchers("/api/account/reset-password/init")
                    .permitAll()
                    .pathMatchers("/api/account/reset-password/finish")
                    .permitAll()
                    .pathMatchers("/api/debug/**")
                    .permitAll()
                    .pathMatchers("/api/admin/**")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
                    .pathMatchers("/api/**")
                    .authenticated()
                    .pathMatchers("/services/**")
                    .authenticated()
                    .pathMatchers("/management/health")
                    .permitAll()
                    .pathMatchers("/management/health/**")
                    .permitAll()
                    .pathMatchers("/management/info")
                    .permitAll()
                    .pathMatchers("/management/prometheus")
                    .permitAll()
                    .pathMatchers("/management/**")
                    .hasAuthority(AuthoritiesConstants.ADMIN)
            )
            .oauth2ResourceServer(oauth2 ->
                oauth2.jwt(jwt -> jwt.jwtDecoder(jwtDecoder()).jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8080", "http://localhost:9000", "http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Link", "X-Total-Count", "X-Total-Pages"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        if (keycloakProperties.isEnabled()) {
            String issuerUri = keycloakProperties.getAuthServerUrl() + "/realms/" + keycloakProperties.getRealm();
            log.info("Configuring JWT decoder with Keycloak issuer URI: {}", issuerUri);

            ReactiveJwtDecoder decoder = ReactiveJwtDecoders.fromIssuerLocation(issuerUri);

            // Wrap decoder with error logging
            return token ->
                decoder
                    .decode(token)
                    .doOnError(error -> {
                        log.error("JWT validation failed: {}", error.getMessage(), error);
                        if (error.getMessage() != null) {
                            if (error.getMessage().contains("Invalid token")) {
                                log.error("Token format is invalid");
                            } else if (error.getMessage().contains("Signature")) {
                                log.error("Token signature validation failed - possible issuer/key mismatch");
                            } else if (error.getMessage().contains("expired")) {
                                log.error("Token is expired");
                            }
                        }
                    });
        }
        // This should not be reached when Keycloak is enabled
        throw new IllegalStateException("JWT Decoder not properly configured. Keycloak must be enabled.");
    }

    @Bean
    public Converter<Jwt, Mono<AbstractAuthenticationToken>> jwtAuthenticationConverter() {
        return new KeycloakJwtAuthenticationConverter();
    }

    /**
     * Converter to extract authorities from Keycloak JWT
     */
    static class KeycloakJwtAuthenticationConverter implements Converter<Jwt, Mono<AbstractAuthenticationToken>> {

        private static final Logger log = LoggerFactory.getLogger(KeycloakJwtAuthenticationConverter.class);

        @Override
        public Mono<AbstractAuthenticationToken> convert(Jwt jwt) {
            Set<String> authorities = new HashSet<>();

            // Get roles from realm_access.roles
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                List<String> roles = (List<String>) realmAccess.get("roles");
                authorities.addAll(roles);
                log.debug("Extracted roles from Keycloak token: {}", roles);
            }

            // Convert to GrantedAuthority with ROLE_ prefix
            Collection<GrantedAuthority> grantedAuthorities = authorities
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

            JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt, grantedAuthorities);
            log.debug("Created JwtAuthenticationToken with {} authorities", grantedAuthorities.size());
            return Mono.just(authenticationToken);
        }
    }
}
