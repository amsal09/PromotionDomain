package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Mcoupon;

import java.util.List;


public interface IMasterCouponRepository {
    Integer saveOrUpdate(Mcoupon mcoupon);
    Mcoupon getAllById(String mCouponId, Long amount);
    List<Mcoupon> checkMinimumTransaction(Long amount);
    Mcoupon getCouponNewMember(String description);
}
