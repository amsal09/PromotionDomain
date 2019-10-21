package com.danapprentech.promotion.services.coupon;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class processToData {

    public String updateCouponStatusInDataDomain(JSONObject jsonObject){
        try{
            String couponID = (String) jsonObject.get ("couponId");
            CouponIssue couponIssue = getCouponDetailsById (couponID);
            Coupon coupon = couponRepo.findAllByCouponId (couponID);
            JSONObject json = new JSONObject();
            json.put ("couponId",couponIssue.getCouponId ());
            json.put("couponStatus",couponIssue.getCouponStatus ());
            json.put ("updatedAt",coupon.getUpdateTime ().toString ());
            json.put ("type","UPDATE");
            producer.sendToExchange (json.toString ());
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return "SUCCEED";
    }
}
