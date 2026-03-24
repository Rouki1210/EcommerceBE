package com.example.ecommerceBE.Service;

import com.example.ecommerceBE.entity.Order;

public interface PaymentService {
    public String createVnpayPayment(Order order) throws Exception;

}
