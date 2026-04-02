package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.OrderItemResponse;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.entity.Order;
import com.example.ecommerceBE.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus().name())
                .totalAmount(order.getTotalAmount())
                .shippingAddress(order.getShippingAddress())
                .orderItems(mapOrderItems(order.getOrderItems()))
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private List<OrderItemResponse> mapOrderItems(List<OrderItem> orderItems) {
        if (orderItems == null) return List.of();
        return orderItems.stream()
                .map(this::toOrderItemResponse)
                .collect(Collectors.toList());
    }

    private OrderItemResponse toOrderItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .id(item.getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())))
                .build();
    }
}