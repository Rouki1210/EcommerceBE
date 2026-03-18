package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ChangePasswordResponse;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordMapper {
    public ChangePasswordResponse toChangePasswordResponse() {
        return ChangePasswordResponse.builder()
                .message("Đổi mật khẩu thành công!")
                .build();
    }
}
