package com.example.ecommerceFE.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerceFE.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, String> {
    
}
