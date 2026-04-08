package com.example.ecommerceBE.Dtos;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponse {
    private String productId;
    private String productName;
    private String imageUrl; // Rất hữu ích để Frontend hiển thị hình ảnh giỏ hàng
    private Integer quantity;
    private BigDecimal price;
}