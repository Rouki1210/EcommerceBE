package com.example.ecommerceBE.Dtos.Auth;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String firstName;
    private String lastName;
    private String accessToken;
    private String refreshToken;
}