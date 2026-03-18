package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.UserResponse;
import com.example.ecommerceBE.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }
}
