package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    // User - xem danh sách order của mình
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> getMyOrders(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getMyOrders(authHeader));
    }

    // User - xem chi tiết 1 order của mình
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getMyOrderById(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.getMyOrderById(id, authHeader));
    }

    // User - tạo order từ cart
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(authHeader, request));
    }

    // User - hủy order
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(
            @PathVariable String id,
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(orderService.cancelOrder(id, authHeader));
    }
}
