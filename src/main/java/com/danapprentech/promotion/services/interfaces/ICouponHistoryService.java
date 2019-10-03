package com.danapprentech.promotion.services.interfaces;

import com.danapprentech.promotion.models.Couponhistory;
import org.json.simple.JSONObject;

public interface ICouponHistoryService {
    Couponhistory getDataByPaymentId(String paymentId);
    String addHistory(JSONObject jsonObject);
}
