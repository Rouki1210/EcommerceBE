package com.example.ecommerceBE.Util;

import com.example.ecommerceBE.entity.Product;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceUtil {

    // Đổi thành public static và nhận Product làm đầu vào
    public static BigDecimal getActivePrice(Product product) {
        LocalDateTime now = LocalDateTime.now();

        // Nếu có giá gốc VÀ có cài đặt ngày tháng
        if (product.getOriginalPrice() != null && product.getSaleStartDate() != null && product.getSaleEndDate() != null) {

            // Nếu đã hết hạn Sale -> Trả về giá gốc ban đầu
            if (now.isAfter(product.getSaleEndDate())) {
                return product.getOriginalPrice();
            }
            // Nếu chưa tới giờ Sale -> Trả về giá gốc ban đầu
            if (now.isBefore(product.getSaleStartDate())) {
                return product.getOriginalPrice();
            }
        }

        // Còn lại: Nằm trong giờ Sale, hoặc sản phẩm không có Sale -> Lấy giá price hiện tại
        return product.getPrice();
    }
}