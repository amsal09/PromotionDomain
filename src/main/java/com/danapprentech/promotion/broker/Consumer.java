package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.controllers.CouponController;
import com.danapprentech.promotion.exception.ResourceNotFoundException;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.response.BaseResponse;
import com.danapprentech.promotion.services.interfaces.ICouponHistoryService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class Consumer {
    private static final Logger logger = LoggerFactory.getLogger(Consumer.class);
    @Autowired
    private CouponController couponController;
    private ICouponHistoryService iCouponHistoryService;
    private JSONParser jsonParser = new JSONParser();

    @RabbitListener(queues = "${promotion.rabbitmq.queue}")
    public void receiveMsgCreateCoupon(byte[] message) {
        JSONObject data = null;
        try {
            String response = new String(message);
            logger.info ("message body from payment: {} ",response);
            Object obj = jsonParser.parse(response);
            data = (JSONObject) obj;
            String paymentId = (String) data.get ("paymentId");
            logger.info ("Try to get coupon history with payment id: {}",paymentId);

            Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);

        }catch (Exception e){
            assert data != null;
            String status = (String) data.get ("status");
            if(status.equalsIgnoreCase ("success")){
                logger.info ("Try to save coupon history with payment id: {}",data.get ("paymentId"));
                BaseResponse baseResponse = couponController.createCoupon (data);
                if(baseResponse.getMessage ().equalsIgnoreCase ("success")){
                    logger.info ("try to publish data to queue");
                    Producer producer = new Producer ("queue.payment");
                    JSONObject json = new JSONObject ();
                    json.put ("paymentId",data.get ("paymentId"));
                    json.put ("domain","promotion");
                    producer.sendToExchange (json.toString ());
                }
            }else{
                logger.info ("Try to update coupon status with coupon id: {}",data.get ("couponId"));
                BaseResponse baseResponse = couponController.updateCouponStatusTrue (data);
            }
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }


    }

}
