package com.example.ecommerceBE.Service.Interface;

import com.example.ecommerceBE.entity.Order;

public interface IPaypalService {
    public String createPaypalPayment(Order order) throws Exception;
    public String capture(String token) throws Exception;
}
