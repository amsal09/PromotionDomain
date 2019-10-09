package com.danapprentech.promotion.repositories.interfaces;

import com.danapprentech.promotion.models.Redeemhistory;
import org.json.simple.JSONObject;

public interface IRedeemHistoryRepository {
    Redeemhistory getRedeemHistoryByPaymentId(String paymentId);
    Integer saveRedeemCouponHistory(JSONObject jsonObject);
}
