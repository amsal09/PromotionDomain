package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemberCouponListener {
    private static final Logger logger = LoggerFactory.getLogger(MemberCouponListener.class);
    private Producer producer = new Producer ("queue.member.promotion");

    @Autowired
    private ICouponService iCouponService;

    @RabbitListener(queues = "queue.member.promotion")
    @RabbitHandler
    public void receiveMsg(String message){
        logger.info ("====== GENERATE NEW COUPON FOR NEW MEMBER");
        try {
            JSONParser parser = new JSONParser();
            JSONObject data = (JSONObject) parser.parse(message);
            logger.info ("message body from member: {} ",data);
            int value = iCouponService.firstCoupon (data);
            if(value!=1){
                logger.info ("insert data failed");
                producer.sendToExchange (message);
            }
        }catch (Exception e){

        }
    }
}
