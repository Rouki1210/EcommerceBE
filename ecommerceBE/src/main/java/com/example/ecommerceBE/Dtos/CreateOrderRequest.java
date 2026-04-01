package com.example.ecommerceBE.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderRequest {
    @NotBlank(message = "địa chỉ giao hàng không được để trống")
    private String shippingAddress;
    private String couponCode;
}
