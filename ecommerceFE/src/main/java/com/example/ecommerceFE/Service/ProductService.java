package com.example.ecommerceFE.Service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ecommerceFE.Dtos.ProductRequest;
import com.example.ecommerceFE.Repository.CategoryRepository;
import com.example.ecommerceFE.Repository.ProductRepository;
import com.example.ecommerceFE.entity.Product;
import com.example.ecommerceFE.entity.Category;

@Service
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
        .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
            product.setName(productRequest.getName());
            product.setDescription(productRequest.getDescription());
            product.setPrice(new BigDecimal(productRequest.getPrice()));
            product.setStock(Integer.parseInt(productRequest.getStock()));
            product.setSku(productRequest.getSku());
            product.setImageUrl(productRequest.getImageUrl());
            product.setCategory(category);

        return productRepository.save(product);
    }

}
