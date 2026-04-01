package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping()
    public OrderResponse createOrder(@RequestBody CreateOrderRequest request){
        Order order =  orderService.createOrder(request);
        return orderService.mapToResponse(order);
    }

    @GetMapping("/{id}")
    public OrderResponse getOrderById(@PathVariable String id){
        Order order = this.orderService.getOrderById(id);
        return  orderService.mapToResponse(order);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Update order status to PAID (for payment callback)
    @PutMapping("/{id}/paid")
    public String markPaid(@PathVariable String id) {

        orderService.markPair(id);

        return "Order marked as PAID";
    }
    // API Lấy danh sách đơn hàng của 1 user cụ thể
    @GetMapping("/my-orders")
    public List<OrderResponse> getMyOrders(Principal principal) {
        // principal.getName() sẽ tự động lấy ra Email (hoặc Username)
        // mà bạn đã lưu vào bên trong Token lúc đăng nhập
        String email = principal.getName();

        return orderService.getMyOrders(email);
    }
}
