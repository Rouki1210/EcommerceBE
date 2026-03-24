package com.example.ecommerceBE.Service.Interface;

import java.util.List;
import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Dtos.ProductResponse;
import com.example.ecommerceBE.Dtos.ProductSaleRequest;
import com.example.ecommerceBE.entity.Product;

public interface IProductService {
    List<ProductResponse> getAllProducts();
    Product getProductById(String id);
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(String id, ProductRequest productRequest);
    void deleteProduct(String id);
    Product applySale(String productId, ProductSaleRequest request);
    Product removeSale(String productId);
}