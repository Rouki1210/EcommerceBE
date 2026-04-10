package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.ProductRequest;
import com.example.ecommerceBE.Dtos.ProductResponsive;
import com.example.ecommerceBE.entity.Category;
import com.example.ecommerceBE.entity.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

public class ProductMapper {

    public static ProductResponsive mapToResponse(Product product) {

        ProductResponsive response = new ProductResponsive();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice().toString());
        response.setStock(product.getStock().toString());
        response.setSku(product.getSku());
        response.setImageUrl(product.getImageUrl());

        if (product.getCategory() != null) {
            response.setCategoryId(product.getCategory().getId());
        }

        if (product.getSizes() != null) {
            response.setSize(product.getSizes().toArray(new String[0]));
        }

        return response;
    }
    public static void mapRequestToEntity(ProductRequest request, Product product, Category category) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(new BigDecimal(request.getPrice()));
        product.setStock(Integer.parseInt(request.getStock()));
        product.setSku(request.getSku());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        if (product.getSizes() == null) {
            product.setSizes(new ArrayList<>());
        }
        product.getSizes().clear();

        if (request.getSize() != null && request.getSize().length > 0) {
            product.getSizes().addAll(Arrays.asList(request.getSize()));
        }
        product.setOriginalPrice(request.getOriginalPrice());

        if (request.getBadge() != null && !request.getBadge().trim().isEmpty()) {
            try {
                product.setBadge(com.example.ecommerceBE.entity.enums.Badge.valueOf(request.getBadge()));
            } catch (IllegalArgumentException e) {
                product.setBadge(null);
            }
        } else {
            product.setBadge(null);
        }
    }
}