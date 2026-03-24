package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Config.JwtUtil;
import com.example.ecommerceBE.Dtos.Auth.LoginRequest;
import com.example.ecommerceBE.Dtos.Auth.LoginResponse;
import com.example.ecommerceBE.Dtos.Auth.UserResponse;
import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.Role;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.AdminService;
import com.example.ecommerceBE.mapper.LoginMapper;
import com.example.ecommerceBE.mapper.UserMapper;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
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
    public LoginResponse adminLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        if (user.getRole() != Role.ADMIN) {
            throw new RuntimeException("Tài khoản không có quyền truy cập vào trang quản trị");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId(), user.getFirstName(), user.getLastName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return loginMapper.toLoginResponse(user, token, refreshToken);
    }

    @Override
    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        return userMapper.toUserResponse(user);
    }

    @Override
    public String deleteUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));
        userRepository.delete(user);
        return "Xóa user thành công!";
    }

    @Override
    public UserResponse updateRole(String id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với id: " + id));

        try {
            user.setRole(Role.valueOf(role.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Role không hợp lệ: " + role);
        }

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }
}
