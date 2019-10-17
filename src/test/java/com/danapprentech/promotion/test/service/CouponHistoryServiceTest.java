package com.danapprentech.promotion.test.service;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.repositories.interfaces.ICouponHistoryRepository;
import com.danapprentech.promotion.services.interfaces.ICouponHistoryService;
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
public class CouponHistoryServiceTest extends AbstractTest {
    @MockBean
    private ICouponHistoryRepository iCouponHistoryRepository;
    @Autowired
    private ICouponHistoryService iCouponHistoryService;
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getDataByPaymentIdTest_Success(){
        Couponhistory couponhis = Couponhistory.builder ()
                .couponhistoryId ("Id")
                .couponId ("couponId")
                .memberId ("memberId")
                .paymentId ("paymentId")
                .build ();
        when (iCouponHistoryRepository.getDataByPaymentId (anyString ())).thenReturn (couponhis);
        String paymentId = "paymentId";
        Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
        assertNotNull (couponhistory);
        assertTrue (couponhistory.getCouponId ().equals (couponhis.getCouponId ()));
    }
    @Test
    public void getDataByPaymentIdTest_Failed(){
        when (iCouponHistoryRepository.getDataByPaymentId (anyString ())).thenReturn (null);
        String paymentId = "paymentId";
        Couponhistory couponhistory = iCouponHistoryService.getDataByPaymentId (paymentId);
        assertNull (couponhistory);
    }
    @Test
    public void addHistoryTest_Success(){
        when (iCouponHistoryRepository.addHistory (any ())).thenReturn ("Success");
        String response = iCouponHistoryService.addHistory (new JSONObject ());
        assertTrue (response.equals ("Success"));
    }
    @Test
    public void addHistoryTest_Failed(){
        when (iCouponHistoryRepository.addHistory (any ())).thenReturn ("Failed");
        String response = iCouponHistoryService.addHistory (new JSONObject ());
        assertTrue (response.equals ("Failed"));
    }
}
