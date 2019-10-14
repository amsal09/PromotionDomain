package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICouponService {
    CouponIssue getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<CouponIssue> getCouponRecommendation(JSONObject jsonObject);
    String saveOrUpdateCoupon(JSONObject jsonObject);
    Integer updateStatus(JSONObject jsonObject);
    Integer updateStatusTrue(JSONObject jsonObject);
    Integer firstCoupon(JSONObject jsonObject);
    List<Coupon> checkForNewMember(String memberId, String mCouponId);
    Integer deleteById(String couponId);
}
