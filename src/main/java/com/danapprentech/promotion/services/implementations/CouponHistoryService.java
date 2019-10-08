package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.repositories.interfaces.ICouponHistoryRepository;
import com.danapprentech.promotion.services.interfaces.ICouponHistoryService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class CouponHistoryService implements ICouponHistoryService {
    private ICouponHistoryRepository iCouponHistoryRepository;

    @Autowired
    public CouponHistoryService(ICouponHistoryRepository iCouponHistoryRepository) {
        this.iCouponHistoryRepository = iCouponHistoryRepository;
    }

    @Override
    public Couponhistory getDataByPaymentId(String paymentId) {
        return iCouponHistoryRepository.getDataByPaymentId (paymentId);
    }

    @Override
    public String addHistory(JSONObject jsonObject) {
        return iCouponHistoryRepository.addHistory (jsonObject);
    }
}
