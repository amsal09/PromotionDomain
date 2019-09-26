package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
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
}
