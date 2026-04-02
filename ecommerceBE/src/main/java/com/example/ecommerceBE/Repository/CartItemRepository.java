package com.example.ecommerceBE.Repository;

import com.example.ecommerceBE.entity.Cart;
import com.example.ecommerceBE.entity.CartItem;
import com.example.ecommerceBE.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
    void deleteAllByCartId(String cartId);
}
