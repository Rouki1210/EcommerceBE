package com.example.ecommerceBE.Dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {


    private String name;

//    private String description;
//
//
//    private String slug;
//
//    private String imageUrl;

    private Boolean isActive = true;
}