package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.Service.Interface.PaymentService;
import com.example.ecommerceBE.entity.Order;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;

    @PostMapping("/vnpay/{orderId}")
    public String createPayment(@PathVariable String orderId, HttpServletRequest request) throws Exception {

        Order order = orderService.getOrderById(orderId);
        String ipAddr = request.getLocalAddr();

        return paymentService.createVnpayPayment(order, ipAddr);
    }

    @GetMapping("/vnpay-return")
    public String paymentReturn(HttpServletRequest request){
        String responseCode = request.getParameter("vnp_ResponseCode");

        if("00".equals(responseCode)){
            String orderId = request.getParameter("vnp_TxnRef");
            orderService.markPair(orderId);
            return "Payment success";
        }
        return "Payment Failed";
    }

}
