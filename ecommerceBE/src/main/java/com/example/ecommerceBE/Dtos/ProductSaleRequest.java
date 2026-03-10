package com.example.ecommerceBE.Dtos;

import com.example.ecommerceBE.entity.enums.Badge;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSaleRequest {
    private Badge badge;
    private BigDecimal newPrice;
    private Integer discountPercentage;
}