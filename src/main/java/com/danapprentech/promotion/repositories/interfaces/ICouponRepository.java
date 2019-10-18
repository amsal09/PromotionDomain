package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Coupon;
import org.json.simple.JSONObject;

import java.util.List;

public interface ICouponRepository {
    Coupon getCouponDetailsById(String couponID);
    List<Coupon> getAllCoupons();
    List<Coupon> getCouponRecommendation(JSONObject jsonObject);
    JSONObject saveOrUpdate(JSONObject jsonObject);
    Integer updateStatus(JSONObject jsonObject);
    Integer updateStatusTrue(JSONObject jsonObject);
    JSONObject firstCoupon(JSONObject jsonObject);
    List<Coupon> checkForNewMember(String memberId, String mCouponId);
    Integer deleteById(String couponId);

}
