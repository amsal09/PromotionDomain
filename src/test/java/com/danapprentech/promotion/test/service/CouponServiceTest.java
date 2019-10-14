package com.danapprentech.promotion.test.service;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class CouponServiceTest {
    @Autowired
    private ICouponService iCouponService;

    @Test
    public void getDetailCouponTest_Success(){
        String couponId ="TCPN-024f43a0-bd46-4291-b9bd-21f6ada9c6c1";
        CouponIssue couponIssue = iCouponService.getCouponDetailsById (couponId);
        assertNotNull (couponIssue);
    }

    @Test
    public void getDetailCouponTest_Empty(){
        String couponId ="TCPN-0";
        CouponIssue couponIssue = iCouponService.getCouponDetailsById (couponId);
        assertNull (couponIssue);
    }
    @Test
    public void getAllDataTest(){
        List<Coupon> couponList = iCouponService.getAllCoupons ();
        assertFalse (couponList.isEmpty ());
    }

    @Test
    public void getCouponRecommendationTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-01");
        jsonObject.put ("amount",25000);
        List<CouponIssue> list = iCouponService.getCouponRecommendation (jsonObject);
        assertFalse (list.isEmpty ());
    }
    @Test
    public void getCouponRecommendationTest_Error(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-0Z");
        jsonObject.put ("amount",25000);
        List<CouponIssue> list = iCouponService.getCouponRecommendation (jsonObject);
        assertTrue(list.isEmpty ());
    }
    @Test
    public void generateCouponTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-01");
        jsonObject.put ("amount",25000);
        String response = iCouponService.saveOrUpdateCoupon (jsonObject);
        assertEquals ("Success",response);
    }
    @Test
    public void generateCouponTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        String response = iCouponService.saveOrUpdateCoupon (jsonObject);
        assertEquals ("Failed",response);
    }
    @Test
    public void updateCouponStatusTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        int value =  iCouponService.updateStatus (jsonObject);
        assertEquals (1,value);
    }

    @Test
    public void updateCouponStatusTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        int value =  iCouponService.updateStatus (jsonObject);
        assertEquals (0,value);
    }

    @Test
    public void updateCouponStatusTrueTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        int rows = iCouponService.updateStatusTrue (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        int rows = iCouponService.updateStatusTrue (jsonObject);
        assertEquals (0,rows);
    }
    @Test
    public void generateNewCouponForNewMemberTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","New");
        jsonObject.put ("status","new member");
        int value = iCouponService.firstCoupon (jsonObject);
        assertEquals (1,value);
    }
    @Test
    public void generateNewCouponForNewMemberTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","New");
        jsonObject.put ("status","new member");
        int value = iCouponService.firstCoupon (jsonObject);
        assertEquals (0,value);
    }
    @Test
    public void checkExistTest_Success(){
        List<Coupon> coupon = iCouponService.checkForNewMember("USR-11A","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertTrue (coupon.isEmpty ());
    }
    @Test
    public void checkExistTest_Failed(){
        List<Coupon> coupon = iCouponService.checkForNewMember("USR-03","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertFalse (coupon.isEmpty ());
    }
}
