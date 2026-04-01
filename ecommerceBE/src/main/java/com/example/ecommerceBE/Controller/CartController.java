package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.CartItemRequest;
import com.example.ecommerceBE.Dtos.CartResponse;
import com.example.ecommerceBE.Dtos.UpdateCartItemRequest;
import com.example.ecommerceBE.Service.Interface.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart(
            @RequestHeader("Authorization") String authHeader) {
        return ResponseEntity.ok(cartService.getMyCart(authHeader));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartService.addToCart(authHeader, request));
    }

    @PutMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String cartItemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        return ResponseEntity.ok(cartService.updateCartItem(authHeader, cartItemId, request));
    }

    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<CartResponse> removeFromCart(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String cartItemId) {
        return ResponseEntity.ok(cartService.removeFromCart(authHeader, cartItemId));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(
            @RequestHeader("Authorization") String authHeader) {
        cartService.clearCart(authHeader);
        return ResponseEntity.ok("Xóa giỏ hàng thành công!");
    }
}