package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.ProductResponsive;
import com.example.ecommerceBE.entity.Product;

public class ProductMapper {

    public static ProductResponsive mapToResponse(Product product) {

        ProductResponsive response = new ProductResponsive();

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
}