package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.CartItemResponse;
import com.example.ecommerceBE.Dtos.CartResponse;
import com.example.ecommerceBE.entity.Cart;
import com.example.ecommerceBE.entity.CartItem;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        List<CartItemResponse> items = cart.getCartItems() == null
                ? List.of()
                : cart.getCartItems().stream()
                .map(this::toCartItemResponse)
                .collect(Collectors.toList());

        BigDecimal totalAmount = items.stream()
                .map(CartItemResponse::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalItems = items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        return CartResponse.builder()
                .id(cart.getId())
                .cartItems(items)
                .totalAmount(totalAmount)
                .totalItems(totalItems)
                .build();
    }

    private CartItemResponse toCartItemResponse(CartItem item) {
        return CartItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .imageUrl(item.getProduct().getImageUrl())
                .price(item.getProduct().getPrice())
                .quantity(item.getQuantity())
                .subTotal(item.getProduct().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}