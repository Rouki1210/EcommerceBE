package com.example.ecommerceBE.Dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    private String name;
    private String description;
    private String price;
    private String stock;
    private String sku;
    private String imageUrl;
    private String categoryId;
    private String[] size;
    private BigDecimal originalPrice;
    private String badge;
}
