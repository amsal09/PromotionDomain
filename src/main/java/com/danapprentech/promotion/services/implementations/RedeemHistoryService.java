package com.danapprentech.promotion.services.implementations;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import com.danapprentech.promotion.services.interfaces.IRedeemHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedeemHistoryService implements IRedeemHistoryService {
    @Autowired
    private IRedeemHistoryRepository iRedeemHistoryRepository;

    @Override
    public Redeemhistory getRedeemHistoryByPaymentId(String paymentId) {
        return iRedeemHistoryRepository.getRedeemHistoryByPaymentId (paymentId);
    }
}
