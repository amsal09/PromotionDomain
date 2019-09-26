package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Coupon;

import java.util.List;

public interface ICouponRepository {
    List<Coupon> getCouponDetailsByName(String couponName);
    Coupon getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<Coupon> getCouponRecommendation(String memberPhone,Long amount);
    List<Coupon> getAllCouponByMember(String memberPhone);
    Integer saveOrUpdate(String memberPhone, Long amount);
    Integer updateStatus(String couponID);
}
