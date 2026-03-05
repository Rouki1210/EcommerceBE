package com.example.ecommerceBE.Service;

import java.util.List;

import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();

    Product createProduct(ProductRequest productRequest);
}