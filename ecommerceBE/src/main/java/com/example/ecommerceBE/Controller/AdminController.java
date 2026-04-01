package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.Auth.UserResponse;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Service.Interface.AdminService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(adminService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(adminService.getOrderById(id));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(adminService.updateOrderStatus(id, status));
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable String id) {
        adminService.deleteOrder(id);
        return ResponseEntity.ok("Xóa order Thành công");
    }
}
