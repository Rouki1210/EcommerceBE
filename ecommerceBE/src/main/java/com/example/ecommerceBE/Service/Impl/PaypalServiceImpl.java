package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Service.Interface.IPaypalService;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.entity.Order;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PaypalServiceImpl implements IPaypalService {
    @Autowired
    private PayPalHttpClient client;
    private OrderService orderService;

    public String createPaypalPayment(Order order) throws Exception {

        OrdersCreateRequest request = new OrdersCreateRequest();
        BigDecimal usd = order.getTotalAmount().divide(BigDecimal.valueOf(24000), 2, RoundingMode.HALF_UP);
        request.prefer("return=representation");

        request.requestBody(new OrderRequest()
                .checkoutPaymentIntent("CAPTURE")
                .applicationContext(new ApplicationContext()
                        .returnUrl("http://localhost:8080/api/payment/paypal/success")
                        .cancelUrl("http://localhost:8080/api/payment/paypal/cancel"))
                .purchaseUnits(List.of(
                        new PurchaseUnitRequest()
                                .referenceId(order.getId())
                                .amountWithBreakdown(new AmountWithBreakdown()
                                        .currencyCode("USD")
                                        .value(usd.toString())
                ))));

        HttpResponse<com.paypal.orders.Order> response = client.execute(request);
        com.paypal.orders.Order paypalOrder = response.result();

        for (LinkDescription link : paypalOrder.links()) {
            if ("approve".equals(link.rel())) {
                return link.href();
            }
        }

        return null;
    }

    public String capture(String token) throws Exception {

        OrdersCaptureRequest request = new OrdersCaptureRequest(token);
        request.requestBody(new OrderRequest());

        HttpResponse<com.paypal.orders.Order> response = client.execute(request);
        com.paypal.orders.Order paypalOrder = response.result();

        if ("COMPLETED".equals(paypalOrder.status())) {

            String orderId = paypalOrder.purchaseUnits().get(0).referenceId();

            orderService.markPair(orderId);
            return "SUCCESS";
        }

        return "FAILED";
    }
}
