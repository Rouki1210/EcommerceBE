package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Repository.CategoryRepository;
import com.example.ecommerceBE.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository; // Tạm thời gọi trực tiếp Repo nếu chưa có Service

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }
}