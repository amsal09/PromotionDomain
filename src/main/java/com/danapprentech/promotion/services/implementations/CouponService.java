package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CouponService implements ICouponService {
    private ICouponRepository iCouponRepository;
    @Autowired
    public CouponService(ICouponRepository iCouponRepository) {
        this.iCouponRepository = iCouponRepository;
    }

    @Override
    public CouponIssue getCouponDetailsById(String couponID) {
        return iCouponRepository.getCouponDetailsById (couponID);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return iCouponRepository.getAllCoupons ();
    }

    @Override
    public List<CouponIssue> getCouponRecommendation(JSONObject jsonObject) {
        return iCouponRepository.getCouponRecommendation (jsonObject);
    }


    @Override
    public Integer saveOrUpdateCoupon(JSONObject jsonObject) {
        return iCouponRepository.saveOrUpdate (jsonObject);
    }

    @Override
    public Integer updateStatus(JSONObject jsonObject) {

        return iCouponRepository.updateStatus (jsonObject);
    }

    @Override
    public Integer updateStatusTrue(JSONObject jsonObject) {
        return iCouponRepository.updateStatusTrue (jsonObject);
    }

    @Override
    public Integer firstCoupon(JSONObject jsonObject) {
        return iCouponRepository.firstCoupon (jsonObject);
    }
}
