package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;

import java.util.List;

public interface OrderService{
    List<OrderResponse> getMyOrders(String authHeader);
    OrderResponse getMyOrderById(String id, String authHeader);
    OrderResponse createOrder(String authHeader, CreateOrderRequest request);
    OrderResponse cancelOrder(String id, String authHeader);

    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(String id);
    OrderResponse updateOrderStatus(String id, String status);
    void deleteOrder(String id);
}
