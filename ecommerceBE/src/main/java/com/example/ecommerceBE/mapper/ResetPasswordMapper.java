package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.ResetPasswordResponse;
import com.example.ecommerceBE.constant.MessageConstants;
import jakarta.mail.event.MailEvent;
import org.springframework.stereotype.Component;

@Component
public class ResetPasswordMapper{
    public ResetPasswordResponse toResetPasswordResponse(){
        return ResetPasswordResponse.builder()
                .message(MessageConstants.RESET_PASSWORD_SUCCESS)
                .build();
    }
}
