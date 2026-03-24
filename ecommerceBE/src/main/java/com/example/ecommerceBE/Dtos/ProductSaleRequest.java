package com.example.ecommerceBE.Dtos;

import com.example.ecommerceBE.entity.enums.Badge;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductSaleRequest {
    private Badge badge;
    private BigDecimal newPrice;
    private Integer discountPercentage;
    private LocalDateTime saleStartDate;
    private LocalDateTime saleEndDate;
}