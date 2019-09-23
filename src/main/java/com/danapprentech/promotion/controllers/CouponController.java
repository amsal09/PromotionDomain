package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("promotion")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class CouponController {
    private ICouponService iCouponService;
    @Autowired
    public CouponController(ICouponService iCouponService) {
        this.iCouponService = iCouponService;
    }

    @GetMapping(value = "/all")
    public List<Coupon> getAllCoupon(){
        return iCouponService.getAllCoupons ();
    }
}
