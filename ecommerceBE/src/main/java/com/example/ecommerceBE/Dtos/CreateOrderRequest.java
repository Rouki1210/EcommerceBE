package com.example.ecommerceBE.Dtos;

import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    private List<OrderItemRequest> items;
    private String couponCode;
    private String shippingAddress;
}
