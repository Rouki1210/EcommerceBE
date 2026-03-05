package com.example.ecommerceFE.Service;

import java.util.List;

import com.example.ecommerceFE.Dtos.ProductRequest;
import com.example.ecommerceFE.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();

    Product createProduct(ProductRequest productRequest);
}