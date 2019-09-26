package com.danapprentech.promotion;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromotionApplicationTests {

    @Autowired
    private ICouponService iCouponService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void whenGetAllCoupon_thenReturnCoupon(){
        List<Coupon> couponList=  iCouponService.getAllCoupons ();
        assertEquals (4, couponList.size ());
    }

    @Test
    public void whenGetCouponRecommendation_thenReturnRecommendation(){
        List<Coupon> couponList= iCouponService.getCouponRecommendation ("1",Long.parseLong ("50000"));
        assertEquals ("1",couponList.get (0).getMemberPhone ());
    }

    @Test
    public void whenGetAllCouponByMemberID_thenReturnCouponByMember(){
        List<Coupon> couponList = iCouponService.getAllCouponByMember ("1");
        assertEquals (3,couponList.size ());
    }


}
