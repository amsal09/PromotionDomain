package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICouponService {
    CouponIssue getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<CouponIssue> getCouponRecommendation(JSONObject jsonObject);
    Integer saveOrUpdateCoupon(JSONObject jsonObject);
    Integer updateStatus(JSONObject jsonObject);
    Integer updateStatusTrue(JSONObject jsonObject);
    Integer firstCoupon(JSONObject jsonObject);
}
