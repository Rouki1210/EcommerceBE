package com.example.ecommerceBE.Dtos;

import lombok.Data;
import java.util.List;

import java.math.BigDecimal;

@Data
public class OrderResponse {
        private String id;
        private String orderNumber;
        private String status;
        private BigDecimal totalAmount;
        private String userId;
        private List<OrderItemResponse> items;
}
