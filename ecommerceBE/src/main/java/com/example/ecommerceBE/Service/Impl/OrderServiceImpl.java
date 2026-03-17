package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Dtos.CreateOrderRequest;
import com.example.ecommerceBE.Dtos.OrderItemRequest;
import com.example.ecommerceBE.Dtos.OrderResponse;
import com.example.ecommerceBE.Repository.OrderRepository;
import com.example.ecommerceBE.Repository.ProductRepository;
import com.example.ecommerceBE.Repository.UserRepository;
import com.example.ecommerceBE.Service.OrderService;
import com.example.ecommerceBE.entity.Order;
import com.example.ecommerceBE.entity.OrderItem;
import com.example.ecommerceBE.entity.Product;
import com.example.ecommerceBE.entity.User;
import com.example.ecommerceBE.entity.enums.OrderStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.id.GUIDGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;


    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = new Order();

        order.setOrderNumber("ORD-" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING);

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("user not fount"));
        order.setUser(user);

        order.setShippingAddress(request.getShippingAddress());


        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal total = BigDecimal.ZERO;

        for (OrderItemRequest item : request.getItems()) {

            Product product = productRepository
                    .findById(item.getProductId())
                    .orElseThrow();

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
        order.setTotalAmount(total);

        return orderRepository.save(order);
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void markPair(String orderId) {
        Order order = getOrderById(orderId);
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);
    }

    public OrderResponse mapToResponse(Order order){

        OrderResponse res = new OrderResponse();

        res.setId(order.getId());
        res.setOrderNumber(order.getOrderNumber());
        res.setStatus(order.getStatus().name());
        res.setTotalAmount(order.getTotalAmount());
        res.setUserId(order.getUser().getId());

        return res;
    }
}
