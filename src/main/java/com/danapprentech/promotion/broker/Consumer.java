package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.controllers.CouponController;
import com.danapprentech.promotion.models.Couponhistory;
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

//    @RabbitListener(queues = "${promotion.rabbitmq.queue}")
//    public void receiveMsg(byte[] message) throws Exception {
//        String response = new String(message);
//        System.out.println ("Receive: "+response);
//        Object obj = jsonParser.parse(response);
//        JSONObject data = (JSONObject) obj;
//        logger.info ("message body from payment: {} ",response);
//        couponController.couponRedeem (data);
//    }

    @RabbitListener(queues = "${promotion.rabbitmq.queue}")
    public void receiveMsgCreateCoupon(byte[] message){
        try {
            String response = new String(message);
            System.out.println ("Receive: "+response);
            Object obj = jsonParser.parse(response);
            JSONObject data = (JSONObject) obj;
            String paymentId = (String) data.get ("paymentId");
            Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
            if(couponhistory == null){
                String status = (String) data.get ("status");
                if(status.equalsIgnoreCase ("success")){
                    couponController.createCoupon (data);
                }else{
                    String getResponse = couponController.updateCouponStatusTrue (data);
                    while (getResponse.equalsIgnoreCase ("failed")){
                        getResponse = couponController.updateCouponStatusTrue (data);
                    }
                }
                logger.info ("message body from payment: {} ",response);
            }
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }


    }

}
