package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Service.IProductService;
import com.example.ecommerceBE.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products") // Thêm /api để chuẩn hóa đường dẫn
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}