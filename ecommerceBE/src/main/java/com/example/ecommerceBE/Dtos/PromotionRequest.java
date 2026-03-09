package com.example.ecommerceBE.Dtos;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PromotionRequest {
    private String name;
    private String description;
    private Integer discountPercentage;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<String> productIds;
}