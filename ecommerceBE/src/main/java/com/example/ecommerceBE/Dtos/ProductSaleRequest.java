package com.example.ecommerceBE.Dtos;

import com.example.ecommerceBE.entity.enums.Badge;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductSaleRequest {
    private Badge badge; // Ví dụ: "SALE"

    // Admin chọn 1 trong 2 cách dưới đây:
    private BigDecimal newPrice; // Nhập thẳng giá mới (Ví dụ: 99000)
    private Integer discountPercentage; // HOẶC nhập phần trăm giảm (Ví dụ: 20)
}