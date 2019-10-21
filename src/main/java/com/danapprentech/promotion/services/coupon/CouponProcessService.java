package com.danapprentech.promotion.services.coupon;

import com.danapprentech.promotion.broker.Producer;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.CouponRepo;
import com.danapprentech.promotion.repositories.GenerateCouponHistoryRepo;
import com.danapprentech.promotion.repositories.MasterRepo;
import com.danapprentech.promotion.repositories.RedeemHistoryRepo;
import com.danapprentech.promotion.response.CouponDetail;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.AbstractMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CouponProcessService extends AbstractMethod {
    private static final Logger logger = LoggerFactory.getLogger(CouponProcessService.class);
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private MasterRepo masterRepo;
    @Autowired
    private GenerateCouponHistoryRepo generateCouponHistoryRepo;
    @Autowired
    private RedeemHistoryRepo redeemHistoryRepo;
    @Autowired
    private CouponInfoService couponInfo;
    private Producer producer = new Producer ("queue.promotion.data");

    @Override
    public boolean save(JSONObject jsonObject) {
        boolean isSucceed = false;
        String getResponse = "Failed";
        int returnValue = 0;
        try {
            Number number = (Number) jsonObject.get ("amount");
            Long value = number.longValue ();
            List<Mcoupon> mcoupons = masterRepo.checkMinimumTransaction (value);

            String uniqueId = "TCPN-"+ UUID.randomUUID().toString();
            String memberId = (String) jsonObject.get ("memberId");
            String masterId = mcoupons.get (0).getmCouponId ();
            String time = LocalDate.now ().plusDays (20).toString ();

            returnValue = couponRepo.insertCoupon (uniqueId,masterId,memberId,time);

            if(returnValue == 1) {
                getResponse = "Success";
                CouponDetail couponDetail = couponInfo.getCouponDetailCoupon (uniqueId);
                ObjectMapper mapper = new ObjectMapper ();
                String couponString = mapper.writeValueAsString (couponDetail);
                JSONParser parser = new JSONParser ();
                JSONObject object = (JSONObject) parser.parse (couponString);
                object.put ("type", "CREATE");
                producer.sendToExchange (object.toJSONString ());
                String historyId = "RHCPN-" + UUID.randomUUID ().toString ();
                Couponhistory history = Couponhistory.builder ()
                        .couponhistoryId (historyId)
                        .couponId (uniqueId)
                        .memberId ((String) jsonObject.get ("memberId"))
                        .paymentId ((String) jsonObject.get ("paymentId"))
                        .build ();
                generateCouponHistoryRepo.save (history);
            }
        }catch (Exception e){

        }
        return isSucceed;
    }

    @Override
    public boolean update(JSONObject jsonObject) {
        int rows =0;
        Redeemhistory redeemhistory = null;
        try {
            String couponID = (String) jsonObject.get ("couponId");
            String paymentCode = (String) jsonObject.get ("paymentMethodCode");
            String paymentId = (String) jsonObject.get ("paymentId");
            Integer amount = (Integer) jsonObject.get ("couponAmount");
            String memberId = (String) jsonObject.get ("memberId");
            String uniqueId = "RCPN-"+ UUID.randomUUID().toString();
            LocalDateTime time = LocalDateTime.now ();
            Redeemhistory redeem = Redeemhistory.builder ()
                    .idRedeem (uniqueId)
                    .couponId (couponID)
                    .memberId (memberId)
                    .paymentId (paymentId)
                    .build ();

            redeemhistory = redeemHistoryRepo.findAllByPaymentId (paymentId);
            if(redeemhistory == null){
                logger.info ("Redeem history null");
                CouponIssue couponIssue = couponInfo.getCouponDetailsById (couponID);
                if(!couponIssue.getPaymentMethod ().equalsIgnoreCase ("000")){
                    logger.info ("payment method code doesn't 000");
                    if(paymentCode.equalsIgnoreCase (couponIssue.getPaymentMethod ())){
                        logger.info ("payment method same to data");
                        if(amount.longValue () == couponIssue.getCouponAmount ()){
                            logger.info ("check amount is pass");
                            rows = couponRepo.updateCouponStatus (couponID,memberId,time);
                            if(rows ==1){
                                logger.info ("update coupon status success");
                                Redeemhistory obj = redeemHistoryRepo.save (redeem);
                                updateCouponStatusInDataDomain(jsonObject);
                            }
                        }
                    }
                }else{
                    logger.info ("payment method is 000");
                    if(amount.longValue () == couponIssue.getCouponAmount ()){
                        logger.info ("check amount is pass");
                        rows = couponRepo.updateCouponStatus (couponID,memberId,time);
                        if(rows ==1){
                            logger.info ("update coupon status success");
                            Redeemhistory obj = redeemHistoryRepo.save (redeem);
                            updateCouponStatusInDataDomain(jsonObject);
                        }
                    }
                }
            }
        }catch (Exception e){

        }
        return false;
    }

    @Override
    public boolean delete(String couponId) {
        int value = 0;
        value = couponRepo.deleteAllByCouponId (couponId);
        if(value==1){
            return true;
        }else {
            return false;
        }
    }

    @Transactional
    public Integer rollbackStatusCoupon(JSONObject jsonObject) {
        int response = 0;
        try {
            String couponId = (String) jsonObject.get ("couponId");
            LocalDateTime time = LocalDateTime.now ();
            response = couponRepo.rollbackCouponStatus (couponId,time);
            if(response == 1){
                updateCouponStatusInDataDomain(jsonObject);
            }
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return response;
    }
}
