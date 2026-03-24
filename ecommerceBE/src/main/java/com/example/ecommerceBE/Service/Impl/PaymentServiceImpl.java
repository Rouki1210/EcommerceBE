package com.example.ecommerceBE.Service.Impl;

import com.example.ecommerceBE.Config.VnpayUntil;
import com.example.ecommerceBE.Service.PaymentService;
import com.example.ecommerceBE.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    @Value("${vnpay.tmnCode}")
    private String tmnCode;

    @Value("${vnpay.secretKey}")
    private String secreKey;

    @Value("${vnpay.url}")
    private String vnPayUrl;

    @Value("${vnpay.returnUrl}")
    private String returnUrl;

    public String createVnpayPayment(Order order) throws Exception{
        Map<String, String> params = new HashMap<>();

        params.put("vnp_Version","2.1.0");
        params.put("vnp_Command","pay");
        params.put("vnp_TmnCode", tmnCode);
        params.put("vnp_Amount",order.getTotalAmount()+"");
        params.put("vnp_OrderInfo","Order "+order.getId());
        params.put("vnp_CreateDate",
                new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));

        params.put("vnp_CurrCode", "VND");

        params.put("vnp_Locale", "vn");

        params.put("vnp_OrderType", "other");

        String query = VnpayUntil.buildQuery(params);
        String secureHash = VnpayUntil.hmacSHA512(secreKey, query);

        query += "&vnp_SecureHash" + secureHash;

        String paymentUrl = vnPayUrl + "?" + query;

        return paymentUrl;

    }
}
