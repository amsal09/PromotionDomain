package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICouponRepository {
    CouponIssue getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<CouponIssue> getCouponRecommendation(JSONObject jsonObject);
    Integer saveOrUpdate(JSONObject jsonObject);
    CouponIssue updateStatus(JSONObject jsonObject);
    Integer updateStatusTrue(JSONObject jsonObject);
    Integer firstCoupon(JSONObject jsonObject);
    Coupon checkForNewMember(String memberId, String mCouponId);
}
