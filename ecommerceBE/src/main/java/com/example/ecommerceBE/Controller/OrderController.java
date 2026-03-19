package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public List<OrderResponse> getAllOrders() {
        return orderService.getAllOrders();
    }

    // Update order status to PAID (for payment callback)
    @PutMapping("/{id}/paid")
    public String markPaid(@PathVariable String id) {

        orderService.markPair(id);

        return "Order marked as PAID";
    }
}
