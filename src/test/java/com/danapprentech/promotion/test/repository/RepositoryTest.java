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
public class RepositoryTest {
    @Autowired
    ICouponRepository iCouponRepository;

    @Test
    public void getAllCoupon(){
        List<Coupon> couponList = iCouponRepository.getAllCoupons ();
        assertFalse (couponList.isEmpty ());
    }

    @Test
    public void getAllCouponRecommended(){
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-02");
        jsonObject.put ("amount",25000);
        List<CouponIssue> couponIssueList = iCouponRepository.getCouponRecommendation (jsonObject);
        assertFalse (couponIssueList.isEmpty ());
    }

    @Test
    public void getDetailCouponById(){
        Coupon coupon = iCouponRepository.getCouponDetailsById ("TCPN-07716c66-7fd5-45a3-8e98-44d1c79590a4");
        assertEquals ("USR-01", coupon.getMemberId ());
    }
}
