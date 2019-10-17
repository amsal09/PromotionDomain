package com.danapprentech.promotion.test.service;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import com.danapprentech.promotion.services.interfaces.IRedeemHistoryService;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class RedeemCouponServiceTest extends AbstractTest {
    @Autowired
    private IRedeemHistoryService iRedeemHistoryService;
    @MockBean
    private IRedeemHistoryRepository iRedeemHistoryRepository;

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getHistoryByPaymentIdTest_Success(){
        Redeemhistory redeemhistory = Redeemhistory.builder()
                .paymentId ("Id")
                .idRedeem ("Redeem Id")
                .memberId ("Member Id")
                .couponId ("Coupon Id")
                .build();
        when (iRedeemHistoryRepository.getRedeemHistoryByPaymentId (anyString ())).thenReturn (redeemhistory);
        String paymentId = "payment Id";
        Redeemhistory redeem = iRedeemHistoryService.getRedeemHistoryByPaymentId (paymentId);
        assertTrue (redeem != null);
        assertTrue (redeem.getCouponId ().equals (redeemhistory.getCouponId ()));
    }
    @Test
    public void getHistoryByPaymentIdTest_Failed(){
        when (iRedeemHistoryRepository.getRedeemHistoryByPaymentId (anyString ())).thenReturn (null);
        String paymentId = "payment Id";
        Redeemhistory redeem = iRedeemHistoryService.getRedeemHistoryByPaymentId (paymentId);
        assertTrue (redeem == null);
    }
    @Test
    public void saveHistoryTest_Success(){
        when (iRedeemHistoryRepository.saveRedeemCouponHistory (any ())).thenReturn (1);
        JSONObject jsonObject = new JSONObject ();
        int value = iRedeemHistoryService.saveRedeemCouponHistory (jsonObject);
        assertTrue (value == 1);
    }
    @Test
    public void saveHistoryTest_Failed(){
        when (iRedeemHistoryRepository.saveRedeemCouponHistory (any ())).thenReturn (0);
        JSONObject jsonObject = new JSONObject ();
        int value = iRedeemHistoryService.saveRedeemCouponHistory (jsonObject);
        assertTrue (value == 0);
    }
}
