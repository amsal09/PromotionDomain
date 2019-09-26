package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import com.danapprentech.promotion.services.interfaces.ICouponService;
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
    public List<Coupon> getCouponDetailsByName(String couponName) {
        return iCouponRepository.getCouponDetailsByName (couponName);
    }

    @Override
    public Coupon getCouponDetailsById(String couponID) {
        return iCouponRepository.getCouponDetailsById (couponID);
    }

    @Override
    public List<Coupon> getAllCoupons() {
        return iCouponRepository.getAllCoupons ();
    }

    @Override
    public List<Coupon> getCouponRecommendation(String memberPhone, Long amount) {
        return iCouponRepository.getCouponRecommendation (memberPhone, amount);
    }

    @Override
    public List<Coupon> getAllCouponByMember(String memberPhone) {
        return iCouponRepository.getAllCouponByMember (memberPhone);
    }

    @Override
    public Integer saveOrUpdateCoupon(String memberPhone, Long amount) {
        return iCouponRepository.saveOrUpdate (memberPhone, amount);
    }

    @Override
    public Integer updateStatus(String couponID) {
        return iCouponRepository.updateStatus (couponID);
    }
}
