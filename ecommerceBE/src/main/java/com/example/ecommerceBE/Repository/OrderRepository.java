package com.example.ecommerceBE.Repository;

import com.example.ecommerceBE.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
