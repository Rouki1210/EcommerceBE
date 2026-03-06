package com.example.ecommerceBE.Service;

import java.util.List;
import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(String id); // Mới: Lấy chi tiết
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(String id, ProductRequest productRequest); // Mới: Cập nhật
    void deleteProduct(String id); // Mới: Xóa
}