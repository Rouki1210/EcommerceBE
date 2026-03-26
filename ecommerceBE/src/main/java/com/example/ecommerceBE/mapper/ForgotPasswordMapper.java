package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ForgotPasswordResponse;
import com.example.ecommerceBE.constant.MessageConstants;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordMapper {
    public ForgotPasswordResponse toForgotPasswordResponse(){
        return ForgotPasswordResponse.builder()
                .message(MessageConstants.FORGOT_PASSWORD_SUCCESS)
                .build();
    }
}
