package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ForgotPasswordResponse;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordMapper {
    public ForgotPasswordResponse toForgotPasswordResponse(){
        return ForgotPasswordResponse.builder()
                .message("Email đặt lại mật khẩu đã được gửi!\"")
                .build();
    }
}
