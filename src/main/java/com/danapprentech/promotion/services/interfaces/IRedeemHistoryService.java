package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Redeemhistory;

public interface IRedeemHistoryService {
    Redeemhistory getRedeemHistoryByPaymentId(String paymentId);
}
