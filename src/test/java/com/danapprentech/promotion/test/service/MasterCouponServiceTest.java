package com.danapprentech.promotion.test.service;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.IMasterCouponRepository;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

public class MasterCouponServiceTest extends AbstractTest {
    @MockBean
    private IMasterCouponRepository iMasterCouponRepository;
    @Autowired
    private IMasterCouponService iMasterCouponService;

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getDetailByIdTest_Success(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        when (iMasterCouponRepository.getDetailById (anyString ())).thenReturn (mcoupon);
        String id = "Id";
        Mcoupon obj = iMasterCouponService.getDetailById (id);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getDetailByIdTest_Failed(){
        when (iMasterCouponRepository.getDetailById (anyString ())).thenReturn (null);
        String id = "Id";
        Mcoupon obj = iMasterCouponService.getDetailById (id);
        assertTrue (obj == null);
    }
    @Test
    public void getAllByIdTest_Success(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        when (iMasterCouponRepository.getAllById (anyString (),anyLong ())).thenReturn (mcoupon);
        String id = "Id";
        Mcoupon obj = iMasterCouponService.getAllById (id,200L);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getAllByIdTest_Failed(){
        when (iMasterCouponRepository.getAllById (anyString (),anyLong ())).thenReturn (null);
        String id = "Id";
        Mcoupon obj = iMasterCouponService.getAllById (id,200L);
        assertTrue (obj == null);
    }
    @Test
    public void checkMinimumTest_Success(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        ArrayList<Mcoupon> list = new ArrayList<> ();
        list.add (mcoupon);
        when (iMasterCouponRepository.checkMinimumTransaction (any ())).thenReturn (list);
        String id = "Id";
        List<Mcoupon> listObj = iMasterCouponService.checkMinimumTransaction (200L);
        assertTrue(listObj.get (0).getmCouponId ().equals (mcoupon.getmCouponId ()));
        assertFalse (listObj.isEmpty ());
    }
    @Test
    public void checkMinimumTest_Failed(){
        when (iMasterCouponRepository.checkMinimumTransaction (any ())).thenReturn (new ArrayList<> ());
        String id = "Id";
        List<Mcoupon> listObj = iMasterCouponService.checkMinimumTransaction (200L);
        assertFalse (!listObj.isEmpty ());
    }
    @Test
    public void getDataByDescriptionTest_Success(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        when (iMasterCouponRepository.getCouponNewMember (anyString ())).thenReturn (mcoupon);
        String desc = "coupon description";
        Mcoupon obj = iMasterCouponService.getCouponNewMember (desc);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getDataByDescriptionTest_Failed(){
        when (iMasterCouponRepository.getCouponNewMember (anyString ())).thenReturn (null);
        String desc = "coupon description";
        Mcoupon obj = iMasterCouponService.getCouponNewMember (desc);
        assertTrue (obj == null);
    }
}
