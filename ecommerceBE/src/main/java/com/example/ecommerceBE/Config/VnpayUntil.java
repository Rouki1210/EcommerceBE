package com.example.ecommerceBE.Config;

//import javax.crypto.Mac;
//import javax.crypto.spec.SecretKeySpec;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.*;
//
//public class VnpayUntil {
//
//    public static String hmacSHA512(String key, String data) throws Exception {
//
//        Mac mac = Mac.getInstance("HmacSHA512");
//
//        SecretKeySpec secretKey =
//                new SecretKeySpec(key.getBytes(), "HmacSHA512");
//
//        mac.init(secretKey);
//
//        byte[] bytes = mac.doFinal(data.getBytes());
//
//        StringBuilder hash = new StringBuilder();
//
//        for (byte b : bytes) {
//            hash.append(String.format("%02x", b));
//        }
//
//        return hash.toString();
//    }
//
//    public static String buildQuery(Map<String, String> params) throws Exception {
//
//        List<String> fieldNames = new ArrayList<>(params.keySet());
//        Collections.sort(fieldNames);
//
//        StringBuilder query = new StringBuilder();
//
//        for (String fieldName : fieldNames) {
//
//            String value = params.get(fieldName);
//
//            if (value != null && value.length() > 0) {
//
//                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
//                query.append("=");
//                query.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
//                query.append("&");
//
//            }
//        }
//
//        query.deleteCharAt(query.length() - 1);
//
//        return query.toString();
//    }
//}