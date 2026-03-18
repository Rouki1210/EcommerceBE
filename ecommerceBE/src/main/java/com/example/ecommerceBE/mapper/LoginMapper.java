package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.LoginResponse;
import com.example.ecommerceBE.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public LoginResponse toLoginResponse (User user, String token ){
        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }
}
