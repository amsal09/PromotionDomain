package com.danapprentech.promotion;

import com.danapprentech.promotion.services.interfaces.ICouponService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PromotionApplicationTests {
    @Autowired
    private ICouponService iCouponService;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testGetAllCoupon(){
        iCouponService.getAllCoupons ();
    }

}
