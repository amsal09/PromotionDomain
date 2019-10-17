package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponHistoryService;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class PaymentCouponListener {
    private static final Logger logger = LoggerFactory.getLogger(PaymentCouponListener.class);
    @Autowired
    private ICouponHistoryService iCouponHistoryService;
    @Autowired
    private ICouponService iCouponService;

    private Producer producer = new Producer ("queue.payment.promotion");
    private Producer rollback = new Producer ("queue.payment.rollback");
    private JSONObject json = new JSONObject ();

    @RabbitListener(queues = "queue.payment.promotion")
    @RabbitHandler
    public void receiveMsgCreateCoupon(String message) {
        JSONParser parser = new JSONParser();
        try {
            JSONObject data = (JSONObject) parser.parse(message);
            logger.info ("message body from payment: {} ",data);

            String paymentId = (String) data.get ("paymentId");
            String status = (String) data.get ("status");
            logger.info ("Try to get coupon history with payment id: {}",paymentId);
            Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
            if(couponhistory != null){
                System.out.println ("Exist");
                if(status.equalsIgnoreCase ("failed")){
                    rollbackData (data);
                }else {
                    logger.info ("Coupon has been generated");
                }
            }else{
                if(status.equalsIgnoreCase ("ON_PROGRESS")){
                    logger.info ("Try to save coupon history with payment id: {}",data.get ("paymentId"));
                    String response = iCouponService.saveOrUpdateCoupon (data);
                    if(response.equalsIgnoreCase ("Failed")){
                        logger.info ("Created coupon failed");
                        logger.info ("try to publish data to queue");
                        producer.sendToExchange (message);
                    }else{
                        logger.info ("Created coupon success");

                    }
                }else{
                    logger.info ("Try to update coupon status with coupon id: {}",data.get ("couponId"));
                    rollbackData (data);
                }
            }
        }catch (Exception e){
            logger.warn ("Error: "+e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());
            e.printStackTrace ();
        }
    }
    public void rollbackData(JSONObject jsonObject){
        try{
            JSONObject json = new JSONObject ();
            int responseValue = iCouponService.updateStatusTrue (jsonObject);
            if(responseValue == 1){
                String paymentId =(String) jsonObject.get ("paymentId");
                Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
                if(couponhistory!=null){
                    iCouponService.deleteById (couponhistory.getCouponId ());
                }
                logger.info ("try to publish data rollback to queue success");
                json.put ("paymentId", jsonObject.get ("paymentId"));
                json.put ("domain", "promotion");
                json.put ("status", "Succeed");
                rollback.sendToExchange (json.toString ());
            }else {
                CouponIssue coupon = iCouponService.getCouponDetailsById ((String)jsonObject.get ("couponId"));
                if(coupon==null) {
                    logger.info ("try to publish data rollback to queue failed");
                    json.put ("paymentId", jsonObject.get ("paymentId"));
                    json.put ("domain", "promotion");
                    json.put ("status", "Failed");
                    rollback.sendToExchange (json.toString ());
                }else{
                    logger.info ("try to publish data rollback to queue success");
                    json.put ("paymentId", jsonObject.get ("paymentId"));
                    json.put ("domain", "promotion");
                    json.put ("status", "Succeed");
                    rollback.sendToExchange (json.toString ());
                }
            }
        }catch (Exception e){
            logger.warn ("Error: "+e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());
        }
    }

    public void successBuild(JSONObject jsonObject){
        json.put ("paymentId", jsonObject.get ("paymentId"));
        json.put ("domain", "promotion");
        json.put ("status", "Succeed");
    }
    public void failedBuild(JSONObject jsonObject){
        json.put ("paymentId", jsonObject.get ("paymentId"));
        json.put ("domain", "promotion");
        json.put ("status", "Failed");
    }
}