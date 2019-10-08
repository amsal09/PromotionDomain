package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Redeemhistory;

public interface IRedeemHistoryRepository {
    Redeemhistory getRedeemHistoryByPaymentId(String paymentId);

}
