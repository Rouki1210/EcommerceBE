package com.example.ecommerceFE.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerceFE.Dtos.ProductRequest;
import com.example.ecommerceFE.Service.ProductService;
import com.example.ecommerceFE.entity.Product;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    
    @PostMapping
    public Product createProduct(@RequestBody ProductRequest request) {

        return productService.createProduct(request);
    }
}
