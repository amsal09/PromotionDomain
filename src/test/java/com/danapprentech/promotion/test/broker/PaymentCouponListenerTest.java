package com.danapprentech.promotion.test.broker;

import com.danapprentech.promotion.broker.PaymentCouponListener;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.repositories.GenerateCouponHistoryRepo;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentCouponListenerTest{
    @MockBean
    private GenerateCouponHistoryRepo generateMock;
    @MockBean
    private ICouponService couponMock;
    @Autowired
    private PaymentCouponListener paymentCouponListener;
    @Test
    public void PaymentCouponListenerTest_SuccessAndHistoryNotNull(){
        Couponhistory couponhistory = Couponhistory.builder ()
                .paymentId ("paymentId")
                .memberId ("memberId")
                .couponId ("couponId")
                .couponhistoryId ("Id")
                .build ();

        when (generateMock.findAllByPaymentId (anyString ())).thenReturn (couponhistory);
        when (couponMock.saveOrUpdateCoupon (any ())).thenReturn ("Success");
        JSONObject json = new JSONObject ();
        json.put ("status","ON_PROGRESS");
        json.put ("paymentId","paymentId");
        json.put ("couponId","couponId");
        json.put ("memberId","memberId");

        paymentCouponListener.receiveMsgCreateCoupon (json.toJSONString ());
    }
    @Test
    public void PaymentCouponListenerTest_Success(){
        when (generateMock.findAllByPaymentId (anyString ())).thenReturn (null);
        when (couponMock.saveOrUpdateCoupon (any ())).thenReturn ("Success");
        JSONObject json = new JSONObject ();
        json.put ("status","ON_PROGRESS");
        json.put ("paymentId","paymentId");
        json.put ("couponId","couponId");
        json.put ("memberId","memberId");

        paymentCouponListener.receiveMsgCreateCoupon (json.toJSONString ());
    }
    @Test
    public void PaymentCouponListenerTest_Failed(){
        when (generateMock.findAllByPaymentId (anyString ())).thenReturn (null);
        when (couponMock.saveOrUpdateCoupon (any ())).thenReturn ("Failed");
        JSONObject json = new JSONObject ();
        json.put ("status","ON_PROGRESS");
        json.put ("paymentId","paymentId");
        json.put ("couponId","couponId");
        json.put ("memberId","memberId");

        paymentCouponListener.receiveMsgCreateCoupon (json.toJSONString ());
    }

}
