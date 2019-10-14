package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.controllers.CouponController;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.response.BaseResponse;
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
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @Autowired
    private ICouponHistoryService iCouponHistoryService;
    @Autowired
    private ICouponService iCouponService;

    private JSONParser jsonParser = new JSONParser();

    @RabbitListener(queues = "queue.payment.promotion")
    @RabbitHandler
    public void receiveMsgCreateCoupon(String message) {
        JSONParser parser = new JSONParser();
        Producer producer = new Producer ("queue.payment");
        JSONObject json = new JSONObject ();
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
                }else{
                    logger.info ("try to publish data to queue success");
                    json.put ("paymentId",data.get ("paymentId"));
                    json.put ("domain","promotion");
                    json.put ("status","Succeed");
                    producer.sendToExchange (json.toString ());
                }
            }else{
                if(status.equalsIgnoreCase ("ON_PROGRESS")){
                    logger.info ("Try to save coupon history with payment id: {}",data.get ("paymentId"));
                    String response = iCouponService.saveOrUpdateCoupon (data);
                    if(response.equalsIgnoreCase ("success")){
                        logger.info ("Created coupon success");
                        logger.info ("try to publish data to queue success");
                        json.put ("paymentId",data.get ("paymentId"));
                        json.put ("domain","promotion");
                        json.put ("status","Succeed");
                        producer.sendToExchange (json.toString ());
                    }else{
                        logger.info ("Created coupon failed");
                        logger.info ("try to publish data to queue failed");
                        json.put ("paymentId",data.get ("paymentId"));
                        json.put ("domain","promotion");
                        json.put ("status","Failed");
                        producer.sendToExchange (json.toString ());
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
            Producer rollback = new Producer ("queue.payment.rollback");
            JSONObject json = new JSONObject ();
            int responseValue = iCouponService.updateStatusTrue (jsonObject);
            String couponId = (String)jsonObject.get ("couponId");
            if(responseValue == 1){
                String paymentId =(String) jsonObject.get ("paymentId");
                Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
                if(couponhistory!=null){
                    iCouponService.deleteById (couponhistory.getCouponId ());
                }
                logger.info ("try to publish data rollback to queue success");
                json.put ("paymentId",jsonObject.get ("paymentId"));
                json.put ("domain","promotion");
                json.put ("status","Succeed");
                rollback.sendToExchange (json.toString ());
            }else {
                logger.info ("try to publish data rollback to queue failed");
                json.put ("paymentId",jsonObject.get ("paymentId"));
                json.put ("domain","promotion");
                json.put ("status","Failed");
                rollback.sendToExchange (json.toString ());
            }
        }catch (Exception e){
            logger.warn ("Error: "+e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());
        }
    }
}
