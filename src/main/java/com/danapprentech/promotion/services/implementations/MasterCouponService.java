package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.IMasterCouponRepository;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class MasterCouponService implements IMasterCouponService {
    @Autowired
    private IMasterCouponRepository iMasterCouponRepository;

    @Override
    @Transactional
    public Integer saveOrUpdate(Mcoupon mcoupon) {
        int value = 0;
        value = iMasterCouponRepository.saveOrUpdate (mcoupon);
        return value;
    }

    @Override
    @Transactional
    public Mcoupon getDetailById(String mCouponId) {
        return iMasterCouponRepository.getDetailById (mCouponId);
    }

    @Override
    @Transactional
    public Mcoupon getAllById(String mCouponId, Long amount) {
        return iMasterCouponRepository.getAllById (mCouponId, amount);
    }

    @Override
    @Transactional
    public List<Mcoupon> checkMinimumTransaction(Long amount) {
        return iMasterCouponRepository.checkMinimumTransaction (amount);
    }

    @Override
    public Mcoupon getCouponNewMember(String description) {
        return iMasterCouponRepository.getCouponNewMember (description);
    }
}
