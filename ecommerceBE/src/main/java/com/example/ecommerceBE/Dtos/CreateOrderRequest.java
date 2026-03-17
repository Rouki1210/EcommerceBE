package com.example.ecommerceBE.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private String userId;
    private List<OrderItemRequest> items;
    private String shippingAddress;
}
