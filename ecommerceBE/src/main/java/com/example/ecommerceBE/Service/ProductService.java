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


    @Override
    public Product applySale(String productId, ProductSaleRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));


        if (product.getOriginalPrice() == null) {
            product.setOriginalPrice(product.getPrice());
        }


        if (request.getNewPrice() != null) {
            product.setPrice(request.getNewPrice());

        } else if (request.getDiscountPercentage() != null) {
            BigDecimal original = product.getOriginalPrice();
            BigDecimal percentage = new BigDecimal(request.getDiscountPercentage());

            BigDecimal discountAmount = original.multiply(percentage).divide(new BigDecimal(100));
            BigDecimal calculatedNewPrice = original.subtract(discountAmount);

            product.setPrice(calculatedNewPrice);
        } else {
            throw new RuntimeException("Must enter new price or discount percentage!");
        }

        product.setBadge(request.getBadge() != null ? request.getBadge() : Badge.Sale);

        return productRepository.save(product);
    }

    @Override
    public Product removeSale(String productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getOriginalPrice() != null) {
            product.setPrice(product.getOriginalPrice());
            product.setOriginalPrice(null);
        }

        product.setBadge(null);

        return productRepository.save(product);
    }

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