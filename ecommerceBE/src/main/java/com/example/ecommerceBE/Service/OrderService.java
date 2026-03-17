package com.example.ecommerceBE.Service;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.entity.Order;

import java.util.List;

public interface OrderService{
    Order createOrder(CreateOrderRequest request);
    Order getOrderById(String orderId);
    List<Order> getAllOrders();
    void  markPair(String orderId);
    OrderResponse mapToResponse(Order order);
}
