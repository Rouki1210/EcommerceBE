package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Dtos.Auth.UserResponse;
import com.example.ecommerceBE.constant.MessageConstants;
import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.Role;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.Interface.AdminService;
import lombok.*;
import org.springframework.stereotype.Service;
import com.example.ecommerceBE.mapper.LoginMapper;
import com.example.ecommerceBE.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final LoginMapper loginMapper;

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findByRole(Role.USER)
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponse> getAllAdmins() {
        return userRepository.findByRole(Role.ADMIN)
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND));
        return mapToUserResponse(user);
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND));
        userRepository.delete(user);
        return MessageConstants.DELETE_USER_SUCCESS;
    }

    @Override
    public UserResponse updateRole(String id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(MessageConstants.USER_NOT_FOUND));

        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(MessageConstants.INVALID_ROLE);
        }

        userRepository.save(user);
        return mapToUserResponse(user);
    }

    private UserResponse mapToUserResponse(User user) {
        return userMapper.toUserResponse(user);
    }
}
