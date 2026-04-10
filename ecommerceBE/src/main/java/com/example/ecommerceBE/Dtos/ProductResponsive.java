package com.example.ecommerceBE.Dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponsive {
    private String id;
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
