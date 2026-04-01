package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.Dtos.CartItemRequest;
import com.example.ecommerceBE.Dtos.CartResponse;
import com.example.ecommerceBE.Dtos.UpdateCartItemRequest;

public interface CartService {
    CartResponse getMyCart(String authHeader);
    CartResponse addToCart(String authHeader, CartItemRequest request);
    CartResponse updateCartItem(String authHeader, String cartItemId, UpdateCartItemRequest request);
    CartResponse removeFromCart(String authHeader, String cartItemId);
    void clearCart(String authHeader);
}

