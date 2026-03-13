package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Dtos.Auth.*;
import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.Provider;
import com.example.ecommerceBE.entity.enums.Role;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.AuthService;
import com.example.ecommerceBE.Service.EmailService;
import com.example.ecommerceBE.Config.JwtUtil;
import com.example.ecommerceBE.entity.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã được sử dụng");
        }

        String verifyToken = UUID.randomUUID().toString();

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .provider(Provider.LOCAL)
                .verifyToken(verifyToken)
                .verifyTokenExpiry(LocalDateTime.now().plusHours(24))
                .build();

        userRepository.save(user);
        String fullName = request.getFirstName() + " " + request.getLastName();
        emailService.sendVerifyEmail(user.getEmail(), fullName,verifyToken);

        return RegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .message("Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.")
                .build();
    }

    @Override
    public String verifyEmail(String token) {
        User user = userRepository.findByVerifyToken(token)
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));

        if (user.getVerifyTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn");
        }

        user.setStatus(Status.ACTIVE);
        user.setVerifyToken(null);
        user.setVerifyTokenExpiry(null);
        userRepository.save(user);

        return "Xác thực email thành công!";
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email hoặc mật khẩu không đúng"));

        if (user.getStatus() == Status.INACTIVE) {
            throw new RuntimeException("Tài khoản chưa được xác thực email");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }


        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email không tồn tại"));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String fullName = user.getFirstName() + " " + user.getLastName();
        emailService.sendResetPasswordEmail(user.getEmail(), fullName, resetToken);

        return ForgotPasswordResponse.builder()
                .message("Email đặt lại mật khẩu đã được gửi!")
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Token không hợp lệ"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token đã hết hạn");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return ResetPasswordResponse.builder()
                .message("Đặt lại mật khẩu thành công!")
                .build();
    }

    @Override
    public ChangePasswordResponse changePassword(String authHeader, ChangePasswordRequest request) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractEmail(token);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu hiện tại không đúng");
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu mới không được trùng mật khẩu cũ");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ChangePasswordResponse.builder()
                .message("Đổi mật khẩu thành công!")
                .build();
    }

    @Override
    public UserResponse getMe(String authHeader) {
        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));

        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole().name())
                .status(user.getStatus().name())
                .build();
    }
}