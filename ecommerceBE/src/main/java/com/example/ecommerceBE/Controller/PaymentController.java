package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Service.Impl.PaypalServiceImpl;
import com.example.ecommerceBE.Service.Interface.OrderService;
import com.example.ecommerceBE.Service.Interface.PaymentService;
import com.example.ecommerceBE.entity.Order;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Authenticator;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final PaypalServiceImpl paypalService;

    @Value("${app.frontent.home-url}")
    private String frontEndUrl;

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
    public void paymentReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String responseCode = request.getParameter("vnp_ResponseCode");

        if("00".equals(responseCode)){
            String orderId = request.getParameter("vnp_TxnRef");

            orderService.markPair(orderId);
            response.sendRedirect(frontEndUrl + "/order-confirmation?orderId=" + orderId);
        }else{
            response.sendRedirect(frontEndUrl + "/payment-failed?orderId=" + request.getParameter("vnp_TxnRef"));
        }
    }

    @GetMapping("/paypal/success")
    @PermitAll
    public void success(@RequestParam String token, HttpServletResponse response) throws IOException {
        response.sendRedirect(frontEndUrl + "/order-confirmation?token=" + token);
    }

    @GetMapping("/paypal/failed")
    @PermitAll
    public void failed(@RequestParam String token, HttpServletResponse response) throws IOException {
        response.sendRedirect(frontEndUrl + "/payment-failed?token=" + token);
    }
}
