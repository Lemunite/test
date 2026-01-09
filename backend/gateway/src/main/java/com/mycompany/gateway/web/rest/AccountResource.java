package com.mycompany.gateway.web.rest;

import com.mycompany.gateway.repository.UserRepository;
import com.mycompany.gateway.security.SecurityUtils;
import com.mycompany.gateway.service.KeycloakRegistrationService;
import com.mycompany.gateway.service.MailService;
import com.mycompany.gateway.service.UserService;
import com.mycompany.gateway.service.dto.AdminUserDTO;
import com.mycompany.gateway.service.dto.PasswordChangeDTO;
import com.mycompany.gateway.service.dto.UserDTO;
import com.mycompany.gateway.web.rest.errors.*;
import com.mycompany.gateway.web.rest.vm.KeyAndPasswordVM;
import com.mycompany.gateway.web.rest.vm.ManagedUserVM;
import jakarta.validation.Valid;
import java.util.*;
import java.util.*;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(AccountResource.class);

    private final UserRepository userRepository;

    private final UserService userService;

    private final MailService mailService;

    private final KeycloakRegistrationService keycloakRegistrationService;

    public AccountResource(
        UserRepository userRepository,
        UserService userService,
        MailService mailService,
        KeycloakRegistrationService keycloakRegistrationService
    ) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.keycloakRegistrationService = keycloakRegistrationService;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        LOG.info("REST request to register user: {}", managedUserVM.getLogin());

        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }

        // Try to register in Keycloak first, but don't fail if it's not available
        try {
            keycloakRegistrationService.registerUser(
                managedUserVM.getLogin(),
                managedUserVM.getEmail(),
                managedUserVM.getPassword(),
                managedUserVM.getFirstName(),
                managedUserVM.getLastName()
            );
            LOG.info("User successfully registered in Keycloak: {}", managedUserVM.getLogin());
        } catch (KeycloakRegistrationService.KeycloakRegistrationException e) {
            LOG.warn("Keycloak registration failed: {}. Proceeding with local registration.", e.getMessage());

            // If it's a conflict error, throw it so frontend knows
            if (e.getMessage() != null && e.getMessage().contains("already exists")) {
                throw new RuntimeException("User already exists");
            }

            // For other errors (connection, auth, etc), continue with local registration
            // This allows the system to work even if Keycloak is unavailable
            LOG.warn("Continuing with local database registration only");
        }

        // Register in local database (always happens)
        return userService
            .registerUser(managedUserVM, managedUserVM.getPassword())
            .doOnSuccess(user -> {
                LOG.info("User registered in local database: {}", managedUserVM.getLogin());
                mailService.sendActivationEmail(user);
            })
            .then();
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public Mono<Void> activateAccount(@RequestParam(value = "key") String key) {
        return userService
            .activateRegistration(key)
            .switchIfEmpty(Mono.error(new AccountResourceException("No user was found for this activation key")))
            .then();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public Mono<AdminUserDTO> getAccount() {
        return ReactiveSecurityContextHolder.getContext()
            .map(SecurityContext::getAuthentication)
            .flatMap(authentication -> {
                if (authentication == null) {
                    return Mono.error(new AccountResourceException("User could not be found"));
                }

                String username;
                String email = null;
                String firstName = null;
                String lastName = null;
                Set<String> authorities = new HashSet<>();

                // Handle JWT from Keycloak
                if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
                    username = jwt.getClaimAsString("preferred_username");
                    if (username == null) {
                        username = jwt.getSubject();
                    }
                    email = jwt.getClaimAsString("email");
                    firstName = jwt.getClaimAsString("given_name");
                    lastName = jwt.getClaimAsString("family_name");

                    // Extract roles from realm_access and format with ROLE_ prefix
                    Map<String, Object> realmAccess = jwt.getClaim("realm_access");
                    if (realmAccess != null && realmAccess.containsKey("roles")) {
                        List<String> roles = (List<String>) realmAccess.get("roles");
                        LOG.debug("Keycloak realm_access.roles: {}", roles);
                        roles.stream().map(role -> role.startsWith("ROLE_") ? role : "ROLE_" + role).forEach(authorities::add);
                    } else {
                        LOG.debug("No realm_access.roles found in JWT for user: {}", username);
                    }
                } else {
                    username = authentication.getName();
                }

                // Add Spring authorities
                authorities.addAll(
                    authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet())
                );

                LOG.info("User account retrieved - login: {}, authorities: {}", username, authorities);

                AdminUserDTO userDTO = new AdminUserDTO();
                userDTO.setLogin(username);
                userDTO.setFirstName(firstName);
                userDTO.setLastName(lastName);
                userDTO.setEmail(email);
                userDTO.setActivated(true);
                userDTO.setAuthorities(authorities);
                userDTO.setLangKey("en");

                return Mono.just(userDTO);
            });
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public Mono<Void> saveAccount(@Valid @RequestBody AdminUserDTO userDTO) {
        return SecurityUtils.getCurrentUserLogin()
            .switchIfEmpty(Mono.error(new AccountResourceException("Current user login not found")))
            .flatMap(userLogin ->
                userRepository
                    .findOneByEmailIgnoreCase(userDTO.getEmail())
                    .filter(existingUser -> !existingUser.getLogin().equalsIgnoreCase(userLogin))
                    .hasElement()
                    .flatMap(emailExists -> {
                        if (emailExists) {
                            throw new EmailAlreadyUsedException();
                        }
                        return userRepository.findOneByLogin(userLogin);
                    })
            )
            .switchIfEmpty(Mono.error(new AccountResourceException("User could not be found")))
            .flatMap(user ->
                userService.updateUser(
                    userDTO.getFirstName(),
                    userDTO.getLastName(),
                    userDTO.getEmail(),
                    userDTO.getLangKey(),
                    userDTO.getImageUrl()
                )
            );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public Mono<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/account/reset-password/init")
    public Mono<Void> requestPasswordReset(@RequestBody String mail) {
        return userService
            .requestPasswordReset(mail)
            .doOnSuccess(user -> {
                if (Objects.nonNull(user)) {
                    mailService.sendPasswordResetMail(user);
                } else {
                    // Pretend the request has been successful to prevent checking which emails really exist
                    // but log that an invalid attempt has been made
                    LOG.warn("Password reset requested for non existing mail");
                }
            })
            .then();
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public Mono<Void> finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        return userService
            .completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey())
            .switchIfEmpty(Mono.error(new AccountResourceException("No user was found for this reset key")))
            .then();
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserVM.PASSWORD_MAX_LENGTH
        );
    }
}
