package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.entity.Order;

public interface PaymentService {
    public String createVnpayPayment(Order order, String IpAddr) throws Exception;

}
