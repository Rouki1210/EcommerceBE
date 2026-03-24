package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.Dtos.Auth.*;

public interface AuthService {
    RegisterResponse register(RegisterRequest request);
    String verifyEmail(String token);
    LoginResponse login(LoginRequest request);
    ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request);
    ResetPasswordResponse resetPassword(ResetPasswordRequest request);
    ChangePasswordResponse changePassword(String authHeader, ChangePasswordRequest request);
    UserResponse getMe(String authHeader);
    LoginResponse adminLogin(LoginRequest request);
}