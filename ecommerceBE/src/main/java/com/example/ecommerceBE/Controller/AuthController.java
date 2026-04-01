package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.Auth.*;
import com.example.ecommerceBE.Service.Interface.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Value("${app.frontend.home-url:http://localhost:5173}")
    private String frontendHomeUrl;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/verify")
    public ResponseEntity<Void> verify(@RequestParam String token) {
        authService.verifyEmail(token);
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, URI.create(frontendHomeUrl).toString())
                .build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return ResponseEntity.ok(authService.resetPassword(request));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ChangePasswordResponse> changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.changePassword(authHeader, request));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<UserResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.updateProfile(authHeader, request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(@RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(authService.getMe(authHeader));
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.adminLogin(request));
    }
}
