package com.example.ecommerceBE.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerceBE.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
