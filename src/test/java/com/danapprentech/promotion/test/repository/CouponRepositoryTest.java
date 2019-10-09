package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import com.danapprentech.promotion.response.CouponIssue;
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
public class CouponRepositoryTest {
    @Autowired
    ICouponRepository iCouponRepository;

    @Test
    public void getAllCoupon(){
        List<Coupon> couponList = iCouponRepository.getAllCoupons ();
        assertFalse (couponList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommendedTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-02");
        jsonObject.put ("amount",25000);
        List<CouponIssue> couponIssueList = iCouponRepository.getCouponRecommendation (jsonObject);
        assertFalse (couponIssueList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommendedTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-00");
        jsonObject.put ("amount",25000);
        List<CouponIssue> couponIssueList = iCouponRepository.getCouponRecommendation (jsonObject);
        assertTrue (couponIssueList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommendedTest_Error(){
        List<CouponIssue> couponIssueList = null;
        try {
            JSONObject jsonObject = new JSONObject ();
            couponIssueList = iCouponRepository.getCouponRecommendation (jsonObject);
            assertTrue (couponIssueList.isEmpty ());
        }catch (Exception e){
            assertNull (couponIssueList);
        }
    }

    @Test
    public void getDetailCouponByIdTest_Success(){
        CouponIssue coupon = iCouponRepository.getCouponDetailsById ("TCPN-07716c66-7fd5-45a3-8e98-44d1c79590a4");
        assertEquals ("USR-01", coupon.getMemberId ());
    }

    @Test
    public void getDetailCouponByIdTest_Empty(){
        CouponIssue coupon = iCouponRepository.getCouponDetailsById ("CouponId");
        assertNull (coupon);

    }

    @Test
    public void getDetailCouponByIdTest_Error(){
        CouponIssue coupon = iCouponRepository.getCouponDetailsById ("");
        assertNull (coupon);

    }

    @Test
    public void saveCouponTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","mmr-01");
        jsonObject.put ("amount",40000L);
        int rows = iCouponRepository.saveOrUpdate (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void saveCouponTest_Error(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","mmr-01");
        int rows = iCouponRepository.saveOrUpdate (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void updateCouponStatusTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        int rows = iCouponRepository.updateStatus (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void updateCouponStatusTest_Error(){
        JSONObject jsonObject = new JSONObject ();
        int rows = iCouponRepository.updateStatus (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        int rows = iCouponRepository.updateStatusTrue (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        int rows = iCouponRepository.updateStatusTrue (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void addCouponNewMemberTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-Tes");
        jsonObject.put ("status","new member");
        int rows = iCouponRepository.firstCoupon (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void addCouponNewMemberTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-Test1");
        jsonObject.put ("status","new member");
        int rows = iCouponRepository.firstCoupon (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void checkExistTest_Success(){
        Coupon coupon = iCouponRepository.checkForNewMember("USR11","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertNull (coupon);
    }
    @Test
    public void checkExistTest_Failed(){
        Coupon coupon = iCouponRepository.checkForNewMember("USR-Test1","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertNotNull(coupon);
    }
}
