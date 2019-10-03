package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Mcoupon;

import java.util.List;

public interface IMasterCouponService {
    Integer saveOrUpdate(Mcoupon mcoupon);
    Mcoupon getDetailById(String mCouponId);
    Mcoupon getAllById(String mCouponId, Long amount);
    List<Mcoupon> checkMinimumTransaction(Long amount);
    Mcoupon getCouponNewMember(String description);
}
