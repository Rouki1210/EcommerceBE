package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Config.VnpayUntil;
import com.example.ecommerceBE.Service.Interface.PaymentService;
import com.example.ecommerceBE.entity.Order;
import com.paypal.orders.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.secretKey}")
    private String secretKey;

    @Value("${vnpay.url}")
    private String vnPayUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    public String createVnpayPayment(Order order, String IpAddr) throws Exception{
        Map<String, String> params = new HashMap<>();

        BigDecimal amount = order.getTotalAmount().multiply(BigDecimal.valueOf(100));
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        params.put("vnp_Version","2.1.0");
        params.put("vnp_Command","pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount", amount.toBigInteger().toString());
        params.put("vnp_OrderInfo","Order "+order.getId());
        params.put("vnp_TxnRef", String.valueOf(order.getId()));
        params.put("vnp_ReturnUrl", returnUrl);
        params.put("vnp_IpAddr", IpAddr);

        params.put("vnp_OrderType", "other");

        params.put("vnp_CurrCode", "VND");
        params.put("vnp_Locale", "vn");

        params.put("vnp_CreateDate", formatter.format(new Date()));

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, 15);
        params.put("vnp_ExpireDate", formatter.format(cal.getTime()));

        String query = VnpayUntil.buildQuery(params);
        String secureHash = VnpayUntil.hmacSHA512(secretKey, query);

        return vnPayUrl + "?" + query + "&vnp_SecureHash=" + secureHash;
    }
}
