package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.PromotionRequest;
import com.example.ecommerceBE.Service.PromotionService;
import com.example.ecommerceBE.entity.Promotion;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {
    private final PromotionService promotionService;

    @GetMapping("/active")
    public List<Promotion> getActivePromotions() {
        return promotionService.getActivePromotions();
    }

    @PostMapping
    public Promotion createPromotion(@RequestBody PromotionRequest request) {
        return promotionService.createPromotion(request);
    }
}