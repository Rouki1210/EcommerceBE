package com.example.ecommerceBE.Controller;

import com.example.ecommerceBE.Dtos.CouponRequest;
import com.example.ecommerceBE.Service.CouponService;
import com.example.ecommerceBE.entity.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupons")
@RequiredArgsConstructor
public class CouponController {
    private final CouponService couponService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Coupon createCoupon(@RequestBody CouponRequest request) {
        return couponService.createCoupon(request);
    }

    @GetMapping("/validate/{code}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Coupon validateCoupon(@PathVariable String code) {
        return couponService.validateAndGetCoupon(code);
    }
}