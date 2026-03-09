package com.example.ecommerceBE.Service;

import java.util.List;
import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Dtos.ProductSaleRequest;
import com.example.ecommerceBE.entity.Product;

public interface IProductService {
    List<Product> getAllProducts();
    Product getProductById(String id); // Mới: Lấy chi tiết
    Product createProduct(ProductRequest productRequest);
    Product updateProduct(String id, ProductRequest productRequest); // Mới: Cập nhật
    void deleteProduct(String id); // Mới: Xóa
    Product applySale(String productId, ProductSaleRequest request);
    Product removeSale(String productId); // Cần có hàm để tắt sale, đưa giá về ban đầu
}