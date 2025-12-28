package com.example.travelhub.flightbooking.controller;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.travelhub.flightbooking.models.ApiResponse;
import com.example.travelhub.flightbooking.models.EmailRequest;
import com.example.travelhub.flightbooking.models.LoginRequest;
import com.example.travelhub.flightbooking.models.LoginResponse;
import com.example.travelhub.flightbooking.models.PasswordResetRequest;
import com.example.travelhub.flightbooking.models.RegisterRequest;
import com.example.travelhub.flightbooking.models.RegisterResponse;
import com.example.travelhub.flightbooking.models.UserResponse;
import com.example.travelhub.flightbooking.service.AuthService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<RegisterResponse>> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Register request received for email: {}", request.getEmail());
        return authService.register(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request received for: {}", request.getEmail());
        return authService.login(request)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<UserResponse>> getCurrentUser(@PathVariable Long userId) {
        log.info("Get current user request for userId: {}", userId);
        return authService.getCurrentUser(userId)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    @PostMapping("/logout/{userId}")
    public Mono<ResponseEntity<ApiResponse>> logout(@PathVariable Long userId) {
        log.info("Logout request for userId: {}", userId);
        return authService.logout(userId)
                .map(response -> {
                    if (response.isSuccess()) {
                        return ResponseEntity.ok(response);
                    } else {
                        return ResponseEntity.badRequest().body(response);
                    }
                });
    }

    @PostMapping("/password-reset")
    public Mono<ResponseEntity<ApiResponse>> requestPasswordReset(@Valid @RequestBody PasswordResetRequest request) {
        log.info("Password reset request for email: {}", request.getEmail());
        return authService.requestPasswordReset(request.getEmail())
                .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping("/resend-verification")
    public Mono<ResponseEntity<ApiResponse>> resendVerificationEmail(@Valid @RequestBody EmailRequest request) {
        log.info("Resend verification email request for: {}", request.getEmail());
        return authService.resendVerificationEmail(request.getEmail())
                .map(response -> ResponseEntity.ok(response));
    }
}
