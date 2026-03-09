package com.example.ecommerceBE.Service;

import com.example.ecommerceBE.Dtos.PromotionRequest;
import com.example.ecommerceBE.Repository.ProductRepository;
import com.example.ecommerceBE.Repository.PromotionRepository;
import com.example.ecommerceBE.entity.Product;
import com.example.ecommerceBE.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final ProductRepository productRepository;

    public Promotion createPromotion(PromotionRequest request) {
        List<Product> products = productRepository.findAllById(request.getProductIds());

        Promotion promotion = Promotion.builder()
                .name(request.getName())
                .description(request.getDescription())
                .discountPercentage(request.getDiscountPercentage())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .isActive(true)
                .products(products)
                .build();

        return promotionRepository.save(promotion);
    }

    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now());
    }
}