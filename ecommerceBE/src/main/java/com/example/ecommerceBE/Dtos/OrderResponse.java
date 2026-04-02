package com.example.ecommerceBE.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
        private String id;
        private String orderNumber;
        private String status;
        private BigDecimal totalAmount;
        private String shippingAddress;
        private List<OrderItemResponse> orderItems;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}