package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.Auth.LoginRequest;
import com.example.ecommerceBE.Dtos.Auth.LoginResponse;
import com.example.ecommerceBE.Dtos.Auth.UserResponse;
import com.example.ecommerceBE.Service.AdminService;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PostMapping("/auth/login")
    public ResponseEntity<LoginResponse> adminLogin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(adminService.adminLogin(request));
    }

    @GetMapping("/users/admins")
    public ResponseEntity<List<UserResponse>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(adminService.getUserById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id) {
        return ResponseEntity.ok(adminService.deleteUser(id));
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<UserResponse> updateRole(
            @PathVariable String id,
            @RequestParam String role) {
        return ResponseEntity.ok(adminService.updateRole(id, role));
    }
}
