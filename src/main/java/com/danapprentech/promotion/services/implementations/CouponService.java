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
    public List<Coupon> getAllCoupons() {
        return iCouponRepository.getAllCoupons ();
    }
}
