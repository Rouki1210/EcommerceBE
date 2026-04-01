package com.example.ecommerceBE.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private String id;
    private String productName;
    private int quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
