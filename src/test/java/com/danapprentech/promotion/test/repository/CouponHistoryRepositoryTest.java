package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.repositories.interfaces.ICouponHistoryRepository;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class CouponHistoryRepositoryTest extends AbstractTest {
    @Autowired
    private ICouponHistoryRepository iCouponHistoryRepository;
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getDataByPaymentIdTest_Success(){
        String paymentId="1";
        Couponhistory couponhistory =  iCouponHistoryRepository.getDataByPaymentId (paymentId);
        assertNotNull (couponhistory);
    }

    @Test
    public void getDataByPaymentIdTest_Empty(){
        String paymentId="28398399-9cd6-4d5d-b983-999cd67d5db3";
        Couponhistory couponhistory =  iCouponHistoryRepository.getDataByPaymentId (paymentId);
        assertNull (couponhistory);
    }

    @Test
    public void saveHistoryPaymentTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","payment01");
        String msg = iCouponHistoryRepository.addHistory (jsonObject);
        assertEquals ("Success",msg);
    }

    @Test
    public void saveHistoryPaymentTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","1");
        String msg = iCouponHistoryRepository.addHistory (jsonObject);
        assertEquals ("Failed",msg);
    }
}
