//package com.danapprentech.promotion.test.repository;
//
//import com.danapprentech.promotion.models.Coupon;
//import com.danapprentech.promotion.repositories.implementations.CouponRepository;
//import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
//import com.danapprentech.promotion.test.controller.AbstractTest;
//import org.json.simple.JSONObject;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import javax.annotation.Resource;
//import javax.transaction.Transactional;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@Transactional
//@SpringBootTest
//public class CouponRepositoryTest extends AbstractTest {
//    @Autowired
//    ICouponRepository iCouponRepository;
//    @MockBean
//    CouponRepository couponMock;
//    @Before
//    public void setUp() {
//        super.setUp();
//    }
//
//    @Test
//    public void getDetailById_Exist(){
//        Coupon coupon = Coupon.builder ()
//                .couponId ("couponId")
//                .memberId ("memberId")
//                .mCouponId ("mId")
//                .couponStatus ("available")
//                .couponExpired ("2019-10-4=30")
//                .createTime (LocalDateTime.now ())
//                .updateTime (LocalDateTime.now ())
//                .build ();
//        when (couponMock.getCouponDetailsById (anyString ())).thenReturn (coupon);
//        String id= "TCPN-01";
//        Coupon obj = iCouponRepository.getCouponDetailsById (id);
//        assertTrue (obj.getMemberId ().equals (coupon.getMemberId ()));
//    }
//
//}
