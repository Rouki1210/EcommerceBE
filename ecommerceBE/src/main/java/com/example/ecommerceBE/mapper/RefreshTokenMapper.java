package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.RefreshTokenResponse;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {
    public RefreshTokenResponse toRefreshTokenResponse(String accessToken, String refreshToken) {
        return RefreshTokenResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}
