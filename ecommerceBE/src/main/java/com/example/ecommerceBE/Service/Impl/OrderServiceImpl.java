package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Config.JwtUtil;
import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderItemRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Repository.*;
import com.example.ecommerceBE.Service.Interface.CartService;
import com.example.ecommerceBE.exception.AppException;
import com.example.ecommerceBE.exception.ErrorCode;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.*;
import com.example.ecommerceBE.entity.enums.OrderStatus;
import com.example.ecommerceBE.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CouponRepository couponRepository;
    private final JwtUtil jwtUtil;
    private final OrderMapper orderMapper;

    // ==================== USER ====================

    @Override
    public List<OrderResponse> getMyOrders(String authHeader) {
        User user = extractUser(authHeader);

        return orderRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getMyOrderById(String id, String authHeader) {
        User user = extractUser(authHeader);

        return orderRepository.findByIdAndUser(id, user)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    @Transactional
    public OrderResponse createOrder(String authHeader, CreateOrderRequest request) {
        User user = extractUser(authHeader);

        // Lấy giỏ hàng
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        if (cart.getCartItems() == null || cart.getCartItems().isEmpty()) {
            throw new AppException(ErrorCode.CART_EMPTY);
        }

        // Tạo order
        Order order = new Order();
        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING);
        order.setUser(user);
        order.setShippingAddress(request.getShippingAddress());

        // Tạo order items từ cart items
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            Product product = cartItem.getProduct();

            // Kiểm tra sản phẩm còn active không
            if (!product.getIsActive()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không còn được bán");
            }

            // Kiểm tra stock
            if (product.getStock() < cartItem.getQuantity()) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            // Tạo order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice()); // Chốt giá tại thời điểm mua
            orderItem.setOrder(order);
            orderItems.add(orderItem);

            // Trừ stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            // Tính tổng tiền
            total = total.add(product.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setOrderItems(orderItems);

        // Áp dụng coupon nếu có
        if (request.getCouponCode() != null && !request.getCouponCode().trim().isEmpty()) {
            total = applyCoupon(request.getCouponCode(), total);
        }

        order.setTotalAmount(total);
        Order savedOrder = orderRepository.save(order);

        // Xóa giỏ hàng sau khi đặt hàng
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return orderMapper.toOrderResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String id, String authHeader) {
        User user = extractUser(authHeader);

        Order order = orderRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Chỉ cho phép hủy khi đang PENDING
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Chỉ có thể hủy đơn hàng đang chờ xử lý");
        }

        // Hoàn lại stock
        for (OrderItem orderItem : order.getOrderItems()) {
            Product product = orderItem.getProduct();
            product.setStock(product.getStock() + orderItem.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        return orderMapper.toOrderResponse(order);
    }

    // ==================== ADMIN ====================

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse getOrderById(String id) {
        return orderRepository.findById(id)
                .map(orderMapper::toOrderResponse)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        try {
            order.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Trạng thái đơn hàng không hợp lệ: " + status);
        }

        orderRepository.save(order);
        return orderMapper.toOrderResponse(order);
    }

    @Override
    @Transactional
    public void deleteOrder(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        // Chỉ cho phép xóa khi đã CANCELLED
        if (order.getStatus() != OrderStatus.CANCELLED) {
            throw new RuntimeException("Chỉ có thể xóa đơn hàng đã bị hủy");
        }

        // Hoàn lại stock nếu cần
        orderRepository.delete(order);
    }

    // ==================== PRIVATE ====================

    private User extractUser(String authHeader) {
        String email = jwtUtil.extractEmail(authHeader.substring(7));
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private BigDecimal applyCoupon(String code, BigDecimal total) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));

        if (!coupon.getIsActive()) {
            throw new AppException(ErrorCode.COUPON_NOT_ACTIVE);
        }

        if (coupon.getEndDate().isBefore(LocalDateTime.now())) {
            throw new AppException(ErrorCode.COUPON_EXPIRED);
        }

        if (coupon.getUsageLimit() != null
                && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            throw new AppException(ErrorCode.COUPON_USAGE_EXCEEDED);
        }

        if (coupon.getMinPurchaseAmount() != null
                && total.compareTo(coupon.getMinPurchaseAmount()) < 0) {
            throw new AppException(ErrorCode.COUPON_MIN_ORDER_NOT_MET);
        }

        BigDecimal discount = BigDecimal.ZERO;

        if (coupon.getDiscountAmount() != null) {
            discount = coupon.getDiscountAmount();
        } else if (coupon.getDiscountPercentage() != null) {
            BigDecimal percentage = new BigDecimal(coupon.getDiscountPercentage());
            discount = total.multiply(percentage).divide(new BigDecimal(100));

            if (coupon.getMaxDiscountAmount() != null
                    && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                discount = coupon.getMaxDiscountAmount();
            }
        }

        // Cập nhật lượt dùng
        coupon.setUsedCount(coupon.getUsedCount() + 1);
        if (coupon.getUsageLimit() != null
                && coupon.getUsedCount() >= coupon.getUsageLimit()) {
            coupon.setIsActive(false);
        }
        couponRepository.save(coupon);

        return total.subtract(discount).max(BigDecimal.ZERO);
    }
}
