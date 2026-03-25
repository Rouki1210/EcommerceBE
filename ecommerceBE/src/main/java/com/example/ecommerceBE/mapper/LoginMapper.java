package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.LoginResponse;
import com.example.ecommerceBE.entity.User;
import org.springframework.stereotype.Component;

@Component
public class LoginMapper {
    public LoginResponse toLoginResponse (User user, String token, String refreshToken){
        return LoginResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .Role(user.getRole().name())
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
