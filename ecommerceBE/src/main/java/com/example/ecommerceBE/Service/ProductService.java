package com.example.ecommerceBE.Service;

import java.math.BigDecimal;
import java.util.List;

import com.example.ecommerceBE.Dtos.ProductSaleRequest;
import com.example.ecommerceBE.entity.enums.Badge;
import org.springframework.stereotype.Service;
import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Repository.CategoryRepository;
import com.example.ecommerceBE.Repository.ProductRepository;
import com.example.ecommerceBE.entity.Product;
import com.example.ecommerceBE.entity.Category;

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
    public Product getProductById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    @Override
    public Product createProduct(ProductRequest productRequest) {
        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        mapRequestToProduct(productRequest, product, category);

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(String id, ProductRequest productRequest) {
        Product existingProduct = getProductById(id);

        Category category = categoryRepository.findById(productRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        mapRequestToProduct(productRequest, existingProduct, category);

        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(String id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }

    // Thêm hàm này vào ProductService.java
    @Override
    public Product applySale(String productId, ProductSaleRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // 1. Lưu lại giá gốc: Chỉ lưu nếu originalPrice đang null (chưa từng sale).
        // Nếu đang sale rồi mà admin đổi giá sale khác, thì phải giữ nguyên originalPrice gốc.
        if (product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        }

        // 2. Tính toán giá mới
        if (request.getNewPrice() != null) {
            // Cách 1: Admin nhập thẳng giá mới
            product.setPrice(request.getNewPrice());

        } else if (request.getDiscountPercentage() != null) {
            // Cách 2: Admin nhập phần trăm. Công thức: Giá gốc * (100 - phần trăm) / 100
            BigDecimal original = product.getOriginalPrice();
            BigDecimal percentage = new BigDecimal(request.getDiscountPercentage());

            // Tính số tiền được giảm = Giá gốc * (% / 100)
            BigDecimal discountAmount = original.multiply(percentage).divide(new BigDecimal(100));
            // Giá mới = Giá gốc - Số tiền được giảm
            BigDecimal calculatedNewPrice = original.subtract(discountAmount);

            product.setPrice(calculatedNewPrice);
        } else {
            throw new RuntimeException("Phải nhập giá mới hoặc phần trăm giảm!");
        }

        // 3. Cập nhật Badge
        product.setBadge(request.getBadge() != null ? request.getBadge() : Badge.Sale);

        return productRepository.save(product);
    }

    // Viết thêm hàm này để Admin có thể tắt Sale
    @Override
    public Product removeSale(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        // Nếu đang có giá gốc (đang sale), thì lấy giá gốc đập ngược lại vào giá bán
        if (product.getOriginalPrice() != null) {
            product.setPrice(product.getOriginalPrice());
            product.setOriginalPrice(null); // Xóa giá gốc đi
        }

        product.setBadge(null); // Gỡ nhãn SALE

        return productRepository.save(product);
    }

    // Hàm phụ để tránh lặp code khi gán dữ liệu từ DTO sang Entity
    private void mapRequestToProduct(ProductRequest request, Product product, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(new BigDecimal(request.getPrice()));
        product.setStock(Integer.parseInt(request.getStock()));
        product.setSku(request.getSku());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);
    }
}