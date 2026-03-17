package com.example.ecommerceBE.Service;

import com.example.ecommerceBE.Dtos.Auth.LoginRequest;
import com.example.ecommerceBE.Dtos.Auth.LoginResponse;
import com.example.ecommerceBE.Dtos.Auth.UserResponse;

import java.util.List;

public interface AdminService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(String id);
    String deleteUser(String id);
    UserResponse updateRole(String id, String role);
    List<UserResponse> getAllAdmins();
    LoginResponse adminLogin(LoginRequest request);
}