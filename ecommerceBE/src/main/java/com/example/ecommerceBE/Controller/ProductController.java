package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Dtos.ProductResponsive;
import com.example.ecommerceBE.Dtos.ProductSaleRequest;
import com.example.ecommerceBE.Service.Interface.IProductService;
import com.example.ecommerceBE.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest request) {
        return new ResponseEntity<>(productService.createProduct(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<ProductResponsive> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponsive> getProductById(@PathVariable String id) {
        Product product = productService.getProductById(id);
        ProductResponsive response = com.example.ecommerceBE.mapper.ProductMapper.mapToResponse(product);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ProductResponsive> updateProduct(@PathVariable String id, @RequestBody ProductRequest request) {
        Product product = productService.updateProduct(id, request);
        ProductResponsive response = com.example.ecommerceBE.mapper.ProductMapper.mapToResponse(product);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }

    @PutMapping("/{id}/sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductResponsive applySale(@PathVariable String id, @RequestBody ProductSaleRequest request) {
        Product product = productService.applySale(id, request);
        return com.example.ecommerceBE.mapper.ProductMapper.mapToResponse(product);
    }


    @PutMapping("/{id}/remove-sale")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Product removeSale(@PathVariable String id) {
        return productService.removeSale(id);
    }
}