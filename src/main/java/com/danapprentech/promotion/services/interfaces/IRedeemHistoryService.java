package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Redeemhistory;
import org.json.simple.JSONObject;

public interface IRedeemHistoryService {
    Redeemhistory getRedeemHistoryByPaymentId(String paymentId);
    Integer saveRedeemCouponHistory(JSONObject jsonObject);
}
