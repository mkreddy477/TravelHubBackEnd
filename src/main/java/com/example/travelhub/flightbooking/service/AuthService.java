package com.example.travelhub.flightbooking.service;





import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.travelhub.auth.entity.UserDetails;
import com.example.travelhub.auth.entity.UserLoginDetails;
import com.example.travelhub.auth.repository.AuthUserDetailsRepository;
import com.example.travelhub.auth.repository.UserLoginDetailsRepository;
import com.example.travelhub.flightbooking.models.ApiResponse;
import com.example.travelhub.flightbooking.models.LoginRequest;
import com.example.travelhub.flightbooking.models.LoginResponse;
import com.example.travelhub.flightbooking.models.RegisterRequest;
import com.example.travelhub.flightbooking.models.RegisterResponse;
import com.example.travelhub.flightbooking.models.UserResponse;

import reactor.core.publisher.Mono;

@Service
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final AuthUserDetailsRepository userDetailsRepository;
    private final UserLoginDetailsRepository userLoginDetailsRepository;

    public AuthService(AuthUserDetailsRepository userDetailsRepository, UserLoginDetailsRepository userLoginDetailsRepository) {
        this.userDetailsRepository = userDetailsRepository;
        this.userLoginDetailsRepository = userLoginDetailsRepository;
    }

    private static final Long DEFAULT_ROLE_ID = 2L; // Default role for regular users

    @Transactional
    public Mono<RegisterResponse> register(RegisterRequest request) {
        log.info("Processing registration for email: {}", request.getEmail());

        return userDetailsRepository.existsByPrimaryEmail(request.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.just(RegisterResponse.builder()
                                .success(false)
                                .message("Email already registered")
                                .build());
                    }

                    return userDetailsRepository.existsByUsername(request.getEmail())
                            .flatMap(usernameExists -> {
                                if (usernameExists) {
                                    return Mono.just(RegisterResponse.builder()
                                            .success(false)
                                            .message("Username already exists")
                                            .build());
                                }

                                return createUser(request);
                            });
                })
                .onErrorResume(e -> {
                    log.error("Error during registration: {}", e.getMessage(), e);
                    return Mono.just(RegisterResponse.builder()
                            .success(false)
                            .message("Registration failed: " + e.getMessage())
                            .build());
                });
    }

    private Mono<RegisterResponse> createUser(RegisterRequest request) {
        LocalDateTime now = LocalDateTime.now();

        UserDetails userDetails = UserDetails.builder()
                .roleId(DEFAULT_ROLE_ID)
                .username(request.getEmail())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .primaryEmail(request.getEmail())
                .primaryPhone(request.getPhone() != null ? request.getPhone() : "")
                .isActive(true)
                .createdOn(now)
                .build();

        return userDetailsRepository.save(userDetails)
                .flatMap(savedUser -> {
                    String salt = generateSalt();
                    String hashedPassword = hashPassword(request.getPassword(), salt);

                    UserLoginDetails loginDetails = UserLoginDetails.builder()
                            .userDetailId(savedUser.getUserDetailId())
                            .password(hashedPassword)
                            .passwordSalt(salt)
                            .isLockedOut(false)
                            .failedPasswordAttemptCount(0)
                            .failedPasswordAnswerAttemptCount(0)
                            .createdOn(now)
                            .build();

                    return userLoginDetailsRepository.save(loginDetails)
                            .map(savedLogin -> RegisterResponse.builder()
                                    .userId(savedUser.getUserDetailId())
                                    .username(savedUser.getUsername())
                                    .email(savedUser.getPrimaryEmail())
                                    .firstName(savedUser.getFirstName())
                                    .lastName(savedUser.getLastName())
                                    .success(true)
                                    .message("Registration successful")
                                    .build());
                });
    }

    public Mono<LoginResponse> login(LoginRequest request) {
        log.info("Processing login for: {}", request.getEmail());

        return userDetailsRepository.findByPrimaryEmail(request.getEmail())
                .switchIfEmpty(userDetailsRepository.findByUsername(request.getEmail()))
                .flatMap(user -> {
                    if (!Boolean.TRUE.equals(user.getIsActive())) {
                        return Mono.just(LoginResponse.builder()
                                .success(false)
                                .message("Account is inactive")
                                .build());
                    }

                    return userLoginDetailsRepository.findByUserDetailId(user.getUserDetailId())
                            .flatMap(loginDetails -> {
                                if (Boolean.TRUE.equals(loginDetails.getIsLockedOut())) {
                                    return Mono.just(LoginResponse.builder()
                                            .success(false)
                                            .message("Account is locked")
                                            .build());
                                }

                                String hashedPassword = hashPassword(request.getPassword(), loginDetails.getPasswordSalt());
                                if (!hashedPassword.equals(loginDetails.getPassword())) {
                                    return handleFailedLogin(loginDetails)
                                            .then(Mono.just(LoginResponse.builder()
                                                    .success(false)
                                                    .message("Invalid credentials")
                                                    .build()));
                                }

                                return handleSuccessfulLogin(loginDetails)
                                        .then(Mono.just(LoginResponse.builder()
                                                .userId(user.getUserDetailId())
                                                .username(user.getUsername())
                                                .email(user.getPrimaryEmail())
                                                .firstName(user.getFirstName())
                                                .lastName(user.getLastName())
                                                .success(true)
                                                .message("Login successful")
                                                .build()));
                            });
                })
                .switchIfEmpty(Mono.just(LoginResponse.builder()
                        .success(false)
                        .message("Invalid credentials")
                        .build()))
                .onErrorResume(e -> {
                    log.error("Error during login: {}", e.getMessage(), e);
                    return Mono.just(LoginResponse.builder()
                            .success(false)
                            .message("Login failed: " + e.getMessage())
                            .build());
                });
    }

    private Mono<UserLoginDetails> handleFailedLogin(UserLoginDetails loginDetails) {
        loginDetails.setFailedPasswordAttemptCount(loginDetails.getFailedPasswordAttemptCount() + 1);
        if (loginDetails.getFailedPasswordAttemptCount() >= 5) {
            loginDetails.setIsLockedOut(true);
            loginDetails.setLastLockoutDate(LocalDateTime.now());
        }
        loginDetails.setUpdatedOn(LocalDateTime.now());
        return userLoginDetailsRepository.save(loginDetails);
    }

    private Mono<UserLoginDetails> handleSuccessfulLogin(UserLoginDetails loginDetails) {
        loginDetails.setFailedPasswordAttemptCount(0);
        loginDetails.setLastLoginDate(LocalDateTime.now());
        loginDetails.setUpdatedOn(LocalDateTime.now());
        return userLoginDetailsRepository.save(loginDetails);
    }

    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            String saltedPassword = password + salt;
            byte[] hashedBytes = md.digest(saltedPassword.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public Mono<UserResponse> getCurrentUser(Long userId) {
        log.info("Getting current user for userId: {}", userId);

        return userDetailsRepository.findById(userId)
                .map(user -> UserResponse.builder()
                        .userId(user.getUserDetailId())
                        .username(user.getUsername())
                        .email(user.getPrimaryEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .phone(user.getPrimaryPhone())
                        .success(true)
                        .message("User found")
                        .build())
                .switchIfEmpty(Mono.just(UserResponse.builder()
                        .success(false)
                        .message("User not found")
                        .build()))
                .onErrorResume(e -> {
                    log.error("Error getting current user: {}", e.getMessage(), e);
                    return Mono.just(UserResponse.builder()
                            .success(false)
                            .message("Error retrieving user: " + e.getMessage())
                            .build());
                });
    }

    public Mono<ApiResponse> logout(Long userId) {
        log.info("Processing logout for userId: {}", userId);

        return userLoginDetailsRepository.findByUserDetailId(userId)
                .flatMap(loginDetails -> {
                    loginDetails.setUpdatedOn(LocalDateTime.now());
                    return userLoginDetailsRepository.save(loginDetails)
                            .then(Mono.just(ApiResponse.builder()
                                    .success(true)
                                    .message("Logout successful")
                                    .build()));
                })
                .switchIfEmpty(Mono.just(ApiResponse.builder()
                        .success(false)
                        .message("User not found")
                        .build()))
                .onErrorResume(e -> {
                    log.error("Error during logout: {}", e.getMessage(), e);
                    return Mono.just(ApiResponse.builder()
                            .success(false)
                            .message("Logout failed: " + e.getMessage())
                            .build());
                });
    }

    public Mono<ApiResponse> requestPasswordReset(String email) {
        log.info("Processing password reset request for email: {}", email);

        return userDetailsRepository.findByPrimaryEmail(email)
                .flatMap(user -> {
                    // In a real application, you would:
                    // 1. Generate a password reset token
                    // 2. Store the token with expiration
                    // 3. Send an email with the reset link
                    log.info("Password reset requested for user: {}", user.getUsername());
                    return Mono.just(ApiResponse.builder()
                            .success(true)
                            .message("Password reset email sent if the account exists")
                            .build());
                })
                .switchIfEmpty(Mono.just(ApiResponse.builder()
                        .success(true)
                        .message("Password reset email sent if the account exists")
                        .build()))
                .onErrorResume(e -> {
                    log.error("Error during password reset request: {}", e.getMessage(), e);
                    return Mono.just(ApiResponse.builder()
                            .success(false)
                            .message("Password reset request failed: " + e.getMessage())
                            .build());
                });
    }

    public Mono<ApiResponse> resendVerificationEmail(String email) {
        log.info("Processing resend verification email for: {}", email);

        return userDetailsRepository.findByPrimaryEmail(email)
                .flatMap(user -> {
                    // In a real application, you would:
                    // 1. Check if user is already verified
                    // 2. Generate a new verification token
                    // 3. Send the verification email
                    log.info("Verification email resent for user: {}", user.getUsername());
                    return Mono.just(ApiResponse.builder()
                            .success(true)
                            .message("Verification email sent if the account exists")
                            .build());
                })
                .switchIfEmpty(Mono.just(ApiResponse.builder()
                        .success(true)
                        .message("Verification email sent if the account exists")
                        .build()))
                .onErrorResume(e -> {
                    log.error("Error during resend verification email: {}", e.getMessage(), e);
                    return Mono.just(ApiResponse.builder()
                            .success(false)
                            .message("Resend verification email failed: " + e.getMessage())
                            .build());
                });
    }
}
