package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Dtos.Auth.*;
import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.Provider;
import com.example.ecommerceBE.entity.enums.Role;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.Interface.AuthService;
import com.example.ecommerceBE.Service.EmailService;
import com.example.ecommerceBE.Config.JwtUtil;
import com.example.ecommerceBE.entity.enums.Status;
import com.example.ecommerceBE.exception.AppException;
import com.example.ecommerceBE.exception.ErrorCode;
import com.example.ecommerceBE.mapper.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
    private final UserMapper userMapper;
    private final LoginMapper loginMapper;
    private final RegisterMapper registerMapper;
    private final ForgotPasswordMapper forgotPasswordMapper;
    private final ResetPasswordMapper resetPasswordMapper;
    private final ChangePasswordMapper changePasswordMapper;
    private final RefreshTokenMapper refreshTokenMapper;

    @Value("${app.dev-mode:false}")
    private boolean devMode;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXISTS);
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
                .status(devMode ? Status.ACTIVE : Status.INACTIVE)
                .build();

        userRepository.save(user);
        if (!devMode) {String fullName = request.getFirstName() + " " + request.getLastName();
            emailService.sendVerifyEmail(user.getEmail(), fullName, verifyToken);}


        return registerMapper.toRegisterResponse(user);
    }

    @Override
    public String verifyEmail(String token) {
        User user = userRepository.findByVerifyToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (user.getVerifyTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
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
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (user.getRole() == Role.ADMIN) {
            throw new AppException(ErrorCode.ADMIN_PAGE);
        }

        if (user.getStatus() == Status.INACTIVE) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_VERIFIED);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }


        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId(), user.getFirstName(), user.getLastName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return loginMapper.toLoginResponse(user, token, refreshToken);
    }

    @Override
    public ForgotPasswordResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        String resetToken = UUID.randomUUID().toString();
        user.setResetToken(resetToken);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        String fullName = user.getFirstName() + " " + user.getLastName();
        emailService.sendResetPasswordEmail(user.getEmail(), fullName, resetToken);

        return forgotPasswordMapper.toForgotPasswordResponse();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        User user = userRepository.findByResetToken(request.getToken())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return resetPasswordMapper.toResetPasswordResponse();
    }

    @Override
    public ChangePasswordResponse changePassword(String email, ChangePasswordRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INCORRECT);
        }

        if (passwordEncoder.matches(request.getNewPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return changePasswordMapper.toChangePasswordResponse();
    }

    @Override
    public UserResponse getMe(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @Override
    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new AppException(ErrorCode.INVALID_R_TOKEN);
        }

        User user = userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AppException(ErrorCode.R_TOKEN_NOT_EXIST));

        String newAccessToken = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId(), user.getFirstName(), user.getLastName());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());

        user.setRefreshToken(newRefreshToken);
        userRepository.save(user);

        return refreshTokenMapper.toRefreshTokenResponse(newAccessToken, newRefreshToken);
    }

    @Override
    public UserResponse updateProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public LoginResponse adminLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_CREDENTIALS));

        if (user.getRole() != Role.ADMIN) {
            throw new AppException(ErrorCode.USER_PAGE);
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.INVALID_CREDENTIALS);
        }

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name(), user.getId(), user.getFirstName(), user.getLastName());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail(), user.getId());

        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return loginMapper.toLoginResponse(user, token, refreshToken);
    }
}