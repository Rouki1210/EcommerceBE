package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderItemRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Repository.CouponRepository;
import com.example.ecommerceBE.Repository.OrderRepository;
import com.example.ecommerceBE.Repository.ProductRepository;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.*;
import com.example.ecommerceBE.entity.enums.OrderStatus;
import com.example.ecommerceBE.exception.AppException;
import com.example.ecommerceBE.exception.ErrorCode;
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
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CouponRepository couponRepository;
    private final OrderMapper orderMapper;


    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request, String id) {
        Order order = new Order();

        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        order.setUser(user);

        order.setShippingAddress(request.getShippingAddress());


        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getItems()) {

            if (item.getQuantity() == null || item.getQuantity() <= 0) {
                throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
            }

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

            if (product.getStock() < item.getQuantity()) {
                throw new com.example.ecommerceBE.exception.AppException(com.example.ecommerceBE.exception.ErrorCode.INSUFFICIENT_STOCK);
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setOrder(order);
            orderItem.setPrice(product.getPrice());

            orderItems.add(orderItem);

            BigDecimal itemTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(item.getQuantity()));

            total = total.add(itemTotal);
        }
        order.setOrderItems(orderItems);

        if (request.getCouponCode() !=null && !request.getCouponCode().trim().isEmpty()) {
            Coupon coupon = couponRepository.findByCode(request.getCouponCode())
                    .orElseThrow(() -> new AppException(ErrorCode.COUPON_NOT_FOUND));

            if (!coupon.getIsActive() ||
                coupon.getStartDate().isAfter(LocalDateTime.now()) ||
                coupon.getEndDate().isBefore(LocalDateTime.now())) {
                throw new AppException(ErrorCode.COUPON_NOT_FOUND);
            }

            if (coupon.getMinPurchaseAmount() != null && total.compareTo(coupon.getMinPurchaseAmount()) < 0) {
                throw new AppException(ErrorCode.COUPON_MIN_ORDER_NOT_MET);
            }

            BigDecimal discount = BigDecimal.ZERO;
            if (coupon.getDiscountAmount() != null) {
                discount = coupon.getDiscountAmount();
            }
            else if (coupon.getDiscountPercentage() != null) {
                BigDecimal percentage = new BigDecimal(coupon.getDiscountPercentage());
                discount = total.multiply(percentage).divide(new BigDecimal(100));

                if (coupon.getMaxDiscountAmount() != null && discount.compareTo(coupon.getMaxDiscountAmount()) > 0) {
                    discount = coupon.getMaxDiscountAmount();
                }
            }
            total = total.subtract(discount);
            if (total.compareTo(BigDecimal.ZERO) < 0) {
                total = BigDecimal.ZERO;
            }
            coupon.setUsedCount(coupon.getUsedCount() + 1);
            if (coupon.getUsageLimit() != null && coupon.getUsedCount() >= coupon.getUsageLimit()) {
                coupon.setIsActive(false); // Nếu đã dùng hết lượt thì tự động khóa mã lại
            }
            couponRepository.save(coupon);
        }

        order.setTotalAmount(total);

        return orderRepository.save(order);
    }


    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse) // Gọi hàm cắt tỉa JSON đã viết sẵn trong Service
                .toList();
    }

    @Override
    public void markPair(String orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }

    public OrderResponse mapToResponse(Order order){


        return orderMapper.toOrderResponse(order);

    }

    @Override
    public List<OrderResponse> getMyOrders(String userId) {

        List<Order> userOrders = orderRepository.findByUserId(userId);


        return userOrders.stream()
                .map(orderMapper::toOrderResponse)
                .collect(Collectors.toList());
    }
}
