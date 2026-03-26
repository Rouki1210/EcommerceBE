package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ChangePasswordResponse;
import com.example.ecommerceBE.constant.MessageConstants;
import org.springframework.stereotype.Component;

@Component
public class ChangePasswordMapper {
    public ChangePasswordResponse toChangePasswordResponse() {
        return ChangePasswordResponse.builder()
                .message(MessageConstants.CHANGE_PASSWORD_SUCCESS)
                .build();
    }
}
