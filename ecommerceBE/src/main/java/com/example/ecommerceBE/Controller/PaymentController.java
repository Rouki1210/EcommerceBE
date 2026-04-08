package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Service.Impl.PaypalServiceImpl;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.Service.Interface.PaymentService;
import com.example.ecommerceBE.entity.Order;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.Authenticator;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaypalServiceImpl paypalService;

    @PostMapping("/vnpay/{orderId}")
    public String createPayment(@PathVariable String orderId, HttpServletRequest request) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Order order = orderService.getOrderById(orderId);
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Forbidden");
        }
        String ipAddr = request.getLocalAddr();

        return paymentService.createVnpayPayment(order, ipAddr);
    }

    @GetMapping("/paypal/{orderId}")
    public String createPaypalPayment(@PathVariable String orderId) throws Exception{
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        Order order = orderService.getOrderById(orderId);
        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Forbidden");
        }
        return paypalService.createPaypalPayment(order);
    }

    @GetMapping("/vnpay-return")
    @PermitAll
    public String paymentReturn(HttpServletRequest request){
        String responseCode = request.getParameter("vnp_ResponseCode");

        if("00".equals(responseCode)){
            String orderId = request.getParameter("vnp_TxnRef");
            orderService.markPair(orderId);
            return "Payment success";
        }
        return "Payment Failed";
    }

    @GetMapping("/paypal/success")
    @PermitAll
    public String success(@RequestParam String token) throws Exception {
        return paypalService.capture(token);
    }

}
