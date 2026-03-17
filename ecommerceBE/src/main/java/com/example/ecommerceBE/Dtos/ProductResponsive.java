package com.example.ecommerceBE.Dtos;

import lombok.Data;

@Data
public class ProductResponsive {
    private String name;
    private String description;
    private String price;
    private String stock;
    private String sku;
    private String imageUrl;
    private String categoryId;
    private String[] size;
}
