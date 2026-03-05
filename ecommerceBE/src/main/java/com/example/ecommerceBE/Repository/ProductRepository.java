package com.example.ecommerceBE.Repository;

import com.example.ecommerceBE.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
