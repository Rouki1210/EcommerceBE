package com.example.ecommerceBE.Repository;

import com.example.ecommerceBE.entity.Cart;
import com.example.ecommerceBE.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByUserEmail(String email);
    Optional<Cart> findByUser(User user);
}
