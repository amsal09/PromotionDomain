package com.danapprentech.promotion.services.coupon;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.response.CouponDetail;
import com.danapprentech.promotion.services.AbstractMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AdditionalService extends AbstractMethod {

    @Override
    public boolean save(JSONObject jsonObject) {
        int returnValue = 0;
        try {
            String desc = (String) jsonObject.get ("status");
            Mcoupon mcoupon = masterRepo.findAllByMCouponDescription (desc);
            String memberID = (String) jsonObject.get ("memberId");
            String masterId = mcoupon.getmCouponId ();
            String uniqueId = "TCPN-"+ UUID.randomUUID().toString();
            String time = LocalDateTime.now ().plusDays (10).toString ();
            coupon = checkForNewMember (memberID,mcoupon.getmCouponId ());
            if(coupon.isEmpty ()){
                returnValue = couponRepo.insertCoupon (uniqueId,masterId,memberID,time);
                if(returnValue == 1){
                    logger.info ("insert data coupon success");
                    CouponDetail couponDetail = getCouponDetailCoupon (uniqueId);
                    ObjectMapper mapper = new ObjectMapper ();
                    String couponString = mapper.writeValueAsString (couponDetail);
                    JSONParser parser = new JSONParser ();
                    JSONObject object = (JSONObject) parser.parse (couponString);
                    object.put ("type","CREATE");
                    producer.sendToExchange (object.toJSONString ());
                }
            }
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return false;
    }

    @Override
    public boolean update(JSONObject jsonObject) {
        return false;
    }
}
