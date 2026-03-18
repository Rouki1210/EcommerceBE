package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ResetPasswordResponse;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordMapper{
    public ResetPasswordResponse toResetPasswordResponse(){
        return ResetPasswordResponse.builder()
                .message("Đặt lại mật khẩu thành công!")
                .build();
    }
}
