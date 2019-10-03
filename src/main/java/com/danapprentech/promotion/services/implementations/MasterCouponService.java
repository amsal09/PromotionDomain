package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.IMasterCouponRepository;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MasterCouponService implements IMasterCouponService {
    @Autowired
    private IMasterCouponRepository iMasterCouponRepository;

    @Override
    public Integer saveOrUpdate(Mcoupon mcoupon) {
        return iMasterCouponRepository.saveOrUpdate (mcoupon);
    }

    @Override
    public Mcoupon getDetailById(String mCouponId) {
        return iMasterCouponRepository.getDetailById (mCouponId);
    }

    @Override
    public Mcoupon getAllById(String mCouponId, Long amount) {
        return iMasterCouponRepository.getAllById (mCouponId, amount);
    }

    @Override
    public List<Mcoupon> checkMinimumTransaction(Long amount) {
        return iMasterCouponRepository.checkMinimumTransaction (amount);
    }

    @Override
    public Mcoupon getCouponNewMember(String description) {
        return iMasterCouponRepository.getCouponNewMember (description);
    }
}
