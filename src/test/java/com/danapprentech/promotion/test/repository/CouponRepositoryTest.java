package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.json.simple.JSONObject;
import org.junit.Before;
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
public class CouponRepositoryTest extends AbstractTest {
    @Autowired
    ICouponRepository iCouponRepository;
    @Before
    public void setUp() {
        super.setUp();
    }

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
        List<Coupon> couponList = iCouponRepository.getCouponRecommendation (jsonObject);
        assertFalse (couponList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommendedTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-0W");
        jsonObject.put ("amount",25000);
        List<Coupon> couponList = iCouponRepository.getCouponRecommendation (jsonObject);
        assertTrue (couponList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommendedTest_Error(){
        List<Coupon> couponList = null;
        try {
            JSONObject jsonObject = new JSONObject ();
            couponList = iCouponRepository.getCouponRecommendation (jsonObject);
            assertTrue (couponList.isEmpty ());
        }catch (Exception e){
            assertNull (couponList);
        }
    }

    @Test
    public void getDetailCouponByIdTest_Success(){
        Coupon coupon = iCouponRepository.getCouponDetailsById ("TCPN-07716c66-7fd5-45a3-8e98-44d1c79590a4");
        assertEquals ("USR-01", coupon.getMemberId ());
    }

    @Test
    public void getDetailCouponByIdTest_Empty(){
        Coupon coupon = iCouponRepository.getCouponDetailsById ("CouponId");
        assertNull (coupon);

    }

    @Test
    public void getDetailCouponByIdTest_Error(){
        Coupon coupon = iCouponRepository.getCouponDetailsById ("");
        assertNull (coupon);

    }

    @Test
    public void saveCouponTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","mmr-01");
        jsonObject.put ("masterId","");
        JSONObject rows = iCouponRepository.saveOrUpdate (jsonObject);
        assertTrue ((Integer)rows.get ("value")==1);
    }

    @Test
    public void saveCouponTest_Error(){
        JSONObject rows = iCouponRepository.saveOrUpdate (null);
        assertTrue ((Integer) rows.get ("value") == 0);
    }

    @Test
    public void updateCouponStatusTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","A2");
        jsonObject.put ("memberId","A2");
        int rows = iCouponRepository.updateStatus (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void updateCouponStatusTest_Error(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        int rows = iCouponRepository.updateStatus (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","B1");
        int rows = iCouponRepository.updateStatusTrue (jsonObject);
        assertEquals (1,rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Failed(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","B1");
        int rows = iCouponRepository.updateStatusTrue (jsonObject);
        assertEquals (0,rows);
    }

    @Test
    public void addCouponNewMemberTest_Success(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-TesAX");
        jsonObject.put ("masterId","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        JSONObject json = iCouponRepository.firstCoupon (jsonObject);

        assertEquals (1,(int)json.get ("value"));
    }

    @Test
    public void addCouponNewMemberTest_Failed(){
        JSONObject json = iCouponRepository.firstCoupon (null);
        assertEquals (0,(int)json.get ("value"));
    }

    @Test
    public void checkExistTest_Success(){
        List<Coupon> coupon = iCouponRepository.checkForNewMember("USR-11","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertTrue (coupon.isEmpty ());
    }
    @Test
    public void checkExistTest_Failed(){
        List<Coupon> coupon = iCouponRepository.checkForNewMember("USR-03","MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertFalse(coupon.isEmpty ());
    }
    @Test
    public void deleteDataByIdTest_Success(){
        String couponId = "TCPN-edc45722-34f7-49a2-93aa-675704267fef";
        int value = iCouponRepository.deleteById (couponId);
        assertEquals (1,value);
    }

    @Test
    public void deleteDataByIdTest_Failed(){
        String couponId = "TCPN-c9a41268-0177-4798-86f0-a83e297f5e6a";
        int value = iCouponRepository.deleteById (couponId);
        assertEquals (0,value);
    }
}
