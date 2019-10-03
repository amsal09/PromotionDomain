package com.danapprentech.promotion.repositories.interfaces;


import com.danapprentech.promotion.models.Couponhistory;
import org.json.simple.JSONObject;

public interface ICouponHistoryRepository {
    Couponhistory getDataByPaymentId(String paymentId);
    String addHistory(JSONObject jsonObject);
}
