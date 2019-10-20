package com.danapprentech.promotion.broker;

import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.repositories.GenerateCouponHistoryRepo;
import com.danapprentech.promotion.response.CouponIssue;
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
    private GenerateCouponHistoryRepo generateCouponHistoryRepo;
    @Autowired
    private ICouponService iCouponService;

    private Producer producer = new Producer ("queue.payment.promotion");

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
            Couponhistory couponhistory = generateCouponHistoryRepo.findAllByPaymentId (paymentId);
            if(couponhistory != null){
                System.out.println ("Exist");
                logger.info ("Coupon has been generated");
            }else{
                if(status.equalsIgnoreCase ("SUCCEED")){
                    logger.info ("Try to save coupon history with payment id: {}",data.get ("paymentId"));
                    String response = iCouponService.saveOrUpdateCoupon (data);
                    if(response.equalsIgnoreCase ("Failed")){
                        logger.info ("Created coupon failed");
                        logger.info ("try to publish data to queue");
                        producer.sendToExchange (message);
                    }else{
                        logger.info ("Created coupon success");

                    }
                }
            }
        }catch (Exception e){
            logger.warn ("Error: "+e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());
            e.printStackTrace ();
        }
    }

}