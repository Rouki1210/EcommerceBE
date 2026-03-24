package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.ProductResponse;
import com.example.ecommerceBE.Util.PriceUtil;
import com.example.ecommerceBE.entity.Product;

public class ProductMapper {

    public static ProductResponse mapToResponse(Product product) {

        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(PriceUtil.getActivePrice(product).toString());
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