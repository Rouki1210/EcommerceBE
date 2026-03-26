package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.Auth.RegisterResponse;
import com.example.ecommerceBE.constant.MessageConstants;
import com.example.ecommerceBE.entity.User;
import org.springframework.stereotype.Component;

@Component
public class RegisterMapper {
    public RegisterResponse toRegisterResponse(User user) {
        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message(MessageConstants.REGISTER_SUCCESS)
                .build();
    }
}
