
package com.danapprentech.promotion.test.service;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.MasterRepo;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class MasterServiceTest extends AbstractTest {
    @MockBean
    private MasterRepo masterRepoMock;
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
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        String id = "Id";
        Mcoupon obj = iMasterCouponService.getDetailById (id);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getDetailByIdTest_Failed(){
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (null);
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
        when (masterRepoMock.findAllByMCouponIdAndAndMCouponAmount (anyString (),anyLong ()))
                .thenReturn (mcoupon);

        String id = "Id";
        Mcoupon obj = iMasterCouponService.getAllById (id,200L);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getAllByIdTest_Failed(){
        when (masterRepoMock.findAllByMCouponIdAndAndMCouponAmount (anyString (),anyLong ()))
                .thenReturn (null);

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
        boolean add = list.add (mcoupon);
        when (masterRepoMock.checkMinimumTransaction (anyLong ())).thenReturn (list);
        String id = "Id";
        List<Mcoupon> listObj = iMasterCouponService.checkMinimumTransaction (200L);
        assertTrue(listObj.get (0).getmCouponId ().equals (mcoupon.getmCouponId ()));
        assertFalse (listObj.isEmpty ());
    }
    @Test
    public void checkMinimumTest_Failed(){
        when (masterRepoMock.checkMinimumTransaction (anyLong ())).thenReturn (new ArrayList<> ());
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
        when (masterRepoMock.findAllByMCouponDescription (anyString ())).thenReturn (mcoupon);
        String desc = "coupon description";
        Mcoupon obj = iMasterCouponService.getCouponNewMember (desc);
        assertTrue (obj.getmCouponId ().equals (mcoupon.getmCouponId ()));
    }
    @Test
    public void getDataByDescriptionTest_Failed(){
        when (masterRepoMock.findAllByMCouponDescription (anyString ())).thenReturn (null);
        String desc = "coupon description";
        Mcoupon obj = iMasterCouponService.getCouponNewMember (desc);
        assertTrue (obj == null);
    }
    @Test
    public void addMasterDatTest_Success(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        when (masterRepoMock.insertData (anyString (),anyString (),anyLong (),anyLong (),anyString ()))
                .thenReturn (1);
        int value = iMasterCouponService.saveOrUpdate (mcoupon);
        assertTrue (value==1);
    }
    @Test
    public void addMasterDatTest_Failed(){
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        when (masterRepoMock.insertData (anyString (),anyString (),anyLong (),anyLong (),anyString ()))
                .thenReturn (0);

        int value = iMasterCouponService.saveOrUpdate (mcoupon);
        assertTrue (value==0);
    }
}