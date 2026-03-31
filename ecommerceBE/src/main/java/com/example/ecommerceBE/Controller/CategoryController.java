package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Repository.CategoryRepository;
import com.example.ecommerceBE.entity.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Category createCategory(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    // Sửa danh mục (Update)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category categoryRequest) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        category.setName(categoryRequest.getName());
        // category.setDescription(categoryRequest.getDescription()); // Nếu bạn có trường này
        return ResponseEntity.ok(categoryRepository.save(category));
    }

    // Xóa danh mục (Delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục"));
        categoryRepository.delete(category);
        return ResponseEntity.ok("Đã xóa danh mục thành công");
    }
}