package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.Dtos.Auth.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    void verifyEmail(String token);
    LoginResponse login(LoginRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
    ChangePasswordResponse changePassword(String email, ChangePasswordRequest request);
    UserResponse getMe(String email);
    RefreshTokenResponse refreshToken(RefreshTokenRequest request);
    UserResponse updateProfile(String email, UpdateProfileRequest request);
    LoginResponse adminLogin(LoginRequest request);
}
