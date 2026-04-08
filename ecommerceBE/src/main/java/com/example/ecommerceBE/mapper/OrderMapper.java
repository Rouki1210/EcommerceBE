package com.example.ecommerceBE.mapper;

import com.example.ecommerceBE.Dtos.OrderItemResponse;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.entity.Order;
import com.example.ecommerceBE.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OrderMapper {


    public OrderItemResponse toOrderItemResponse(OrderItem item) {
        OrderItemResponse itemRes = new OrderItemResponse();
        itemRes.setProductId(item.getProduct().getId());
        itemRes.setProductName(item.getProduct().getName());
        itemRes.setImageUrl(item.getProduct().getImageUrl());
        itemRes.setQuantity(item.getQuantity());
        itemRes.setPrice(item.getPrice());
        return itemRes;
    }


    public OrderResponse toOrderResponse(Order order) {
        OrderResponse res = new OrderResponse();
        res.setId(order.getId());
        res.setOrderNumber(order.getOrderNumber());
        res.setStatus(order.getStatus().name());
        res.setTotalAmount(order.getTotalAmount());
        res.setUserId(order.getUser().getId());


        // Gọi lại hàm map chi tiết ở trên bằng Stream API
        if (order.getOrderItems() != null) {
            res.setItems(order.getOrderItems().stream()
                    .map(this::toOrderItemResponse) // Dùng method reference
                    .collect(Collectors.toList()));
        }
        return res;
    }
}