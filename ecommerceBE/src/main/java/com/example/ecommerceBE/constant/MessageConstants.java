package com.example.ecommerceBE.constant;

public class MessageConstants {

    // Auth
    public static final String EMAIL_ALREADY_EXISTS = "Email đã được sử dụng";
    public static final String EMAIL_OR_PASSWORD_INCORRECT = "Email hoặc mật khẩu không đúng";
    public static final String ACCOUNT_NOT_VERIFIED = "Tài khoản chưa được xác thực email";
    public static final String ACCOUNT_NOT_FOUND = "Người dùng không tồn tại";
    public static final String INVALID_TOKEN = "Token không hợp lệ";
    public static final String TOKEN_EXPIRED = "Token đã hết hạn";
    public static final String ADMIN_LOGIN_REQUIRED = "Vui lòng đăng nhập tại trang quản trị";
    public static final String REGISTER_SUCCESS = "Đăng ký thành công! Vui lòng kiểm tra email để xác thực tài khoản.";
    public static final String VERIFY_SUCCESS = "Xác thực email thành công!";
    public static final String FORGOT_PASSWORD_SUCCESS = "Email đặt lại mật khẩu đã được gửi!";
    public static final String RESET_PASSWORD_SUCCESS = "Đặt lại mật khẩu thành công!";
    public static final String CHANGE_PASSWORD_SUCCESS = "Đổi mật khẩu thành công!";
    public static final String CURRENT_PASSWORD_INCORRECT = "Mật khẩu hiện tại không đúng";
    public static final String NEW_PASSWORD_SAME_AS_OLD = "Mật khẩu mới không được trùng mật khẩu cũ";
    public static final String USER_NOT_EXIST = "Người dùng không tồn tại";

    // Admin
    public static final String USER_NOT_FOUND = "Không tìm thấy user với id: ";
    public static final String DELETE_USER_SUCCESS = "Xóa user thành công!";
    public static final String INVALID_ROLE = "Role không hợp lệ: ";
    public static final String ACCESS_DENIED = "Tài khoản không có quyền truy cập vào trang quản trị";
}