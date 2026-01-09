package com.mycompany.gateway.web.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * Diagnostic endpoint for debugging token issues
 */
@RestController
@RequestMapping("/api/debug")
public class TokenDebugResource {

    private static final Logger log = LoggerFactory.getLogger(TokenDebugResource.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Debug endpoint: Decode and display token information
     * GET /api/debug/token-info
     */
    @GetMapping("/token-info")
    public Mono<ResponseEntity<Map<String, Object>>> getTokenInfo(
        @RequestHeader(value = "Authorization", required = false) String authHeader
    ) {
        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || authHeader.isEmpty()) {
            response.put("error", "No Authorization header found");
            return Mono.just(ResponseEntity.ok(response));
        }

        try {
            String token = authHeader.replace("Bearer ", "").trim();
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                response.put("error", "Invalid JWT format - expected 3 parts, got " + parts.length);
                return Mono.just(ResponseEntity.ok(response));
            }

            // Decode header
            String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
            JsonNode header = objectMapper.readTree(headerJson);
            response.put("header", objectMapper.readValue(headerJson, Object.class));

            // Decode payload
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode payload = objectMapper.readTree(payloadJson);
            response.put("payload", objectMapper.readValue(payloadJson, Object.class));

            // Check issuer
            String issuer = payload.has("iss") ? payload.get("iss").asText() : "NOT FOUND";
            response.put("issuer", issuer);

            // Check expiration
            if (payload.has("exp")) {
                long expTime = payload.get("exp").asLong() * 1000;
                long nowTime = System.currentTimeMillis();
                response.put("expired", nowTime > expTime);
                response.put("exp_timestamp", expTime);
                response.put("current_time", nowTime);
                response.put("time_until_expiry_ms", expTime - nowTime);
            }

            // Check realm_access roles
            if (payload.has("realm_access")) {
                response.put("realm_access", objectMapper.readValue(payload.get("realm_access").toString(), Object.class));
            }

            log.info("Token debug info - Issuer: {}, Expired: {}", issuer, response.get("expired"));
        } catch (Exception e) {
            response.put("error", "Failed to decode token: " + e.getMessage());
            log.error("Error decoding token", e);
        }

        return Mono.just(ResponseEntity.ok(response));
    }

    /**
     * Get current authenticated user info
     * GET /api/debug/auth-info
     */
    @GetMapping("/auth-info")
    public Mono<ResponseEntity<Map<String, Object>>> getAuthInfo() {
        return ReactiveSecurityContextHolder.getContext()
            .map(context -> {
                Map<String, Object> info = new HashMap<>();
                info.put("authenticated", context.getAuthentication().isAuthenticated());
                info.put("principal", context.getAuthentication().getPrincipal());
                info.put("authorities", context.getAuthentication().getAuthorities());
                info.put("details", context.getAuthentication().getDetails());
                return ResponseEntity.ok(info);
            })
            .defaultIfEmpty(ResponseEntity.ok(Map.of("error", "No authentication context found")));
    }
}
