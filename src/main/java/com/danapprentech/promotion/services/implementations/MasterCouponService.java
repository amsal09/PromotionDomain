package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.MasterRepo;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class MasterCouponService implements IMasterCouponService {
    @Autowired
    private MasterRepo masterRepo;

    @Override
    @Transactional
    public Integer saveOrUpdate(Mcoupon mcoupon) {
        int value = 0;
        String uniqueId = "MCPN-"+ UUID.randomUUID().toString();
        Mcoupon obj = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId (uniqueId)
                .withMCouponAmount (mcoupon.getmCouponAmount ())
                .withMCouponDesc (mcoupon.getmCouponDescription ())
                .withMCouponMinTransaction (mcoupon.getmMinimumTransaction ())
                .withPaymentMethod (mcoupon.getPaymentMethod ())
                .build ();
        value = masterRepo.insertData (obj.getmCouponId (),obj.getmCouponDescription ()
                ,obj.getmCouponAmount (),obj.getmMinimumTransaction (),obj.getPaymentMethod ());

        return value;
    }

    @Override
    @Transactional
    public Mcoupon getDetailById(String mCouponId) {

        return masterRepo.findAllByMCouponId (mCouponId);
    }

    @Override
    @Transactional
    public Mcoupon getAllById(String mCouponId, Long amount) {
        return masterRepo.findAllByMCouponIdAndAndMCouponAmount (mCouponId,amount);
    }

    @Override
    @Transactional
    public List<Mcoupon> checkMinimumTransaction(Long amount) {

        return masterRepo.checkMinimumTransaction (amount);
    }

    @Override
    public Mcoupon getCouponNewMember(String description) {
        return masterRepo.findAllByMCouponDescription (description);
    }
}
