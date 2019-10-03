package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.controllers.CouponController;
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
    CouponController couponController;
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
    public void receiveMsgCreateCoupon(byte[] message) throws Exception {
        String response = new String(message);
        System.out.println ("Receive: "+response);
        Object obj = jsonParser.parse(response);
        JSONObject data = (JSONObject) obj;
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
        couponController.couponRedeem (data);
    }

}
