package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Coupon;

import java.util.List;

public interface ICouponService {
    List<Coupon> getCouponDetailsByName(String couponName);
    Coupon getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<Coupon> getCouponRecommendation(String memberPhone, Long amount);
    List<Coupon> getAllCouponByMember(String memberPhone);
    Integer saveOrUpdateCoupon(String memberPhone, Long amount);
    Integer updateStatus(String couponID);
}
