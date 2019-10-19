package com.danapprentech.promotion.test.service;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.models.Couponhistory;
import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.CouponRepo;
import com.danapprentech.promotion.repositories.GenerateCouponHistoryRepo;
import com.danapprentech.promotion.repositories.MasterRepo;
import com.danapprentech.promotion.repositories.RedeemHistoryRepo;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class CouponServiceTest extends AbstractTest {
    @MockBean
    private CouponRepo couponRepoMock;
    @MockBean
    private MasterRepo masterRepoMock;
    @MockBean
    private GenerateCouponHistoryRepo generateCouponHistoryRepoMock;
    @MockBean
    private RedeemHistoryRepo redeemHistoryRepoMock;

    @Before
    public void setUp() {
        super.setUp ();
    }

    @Autowired
    private ICouponService iCouponService;

    @Test
    public void getDetailCouponTest_Success() {
        String couponId = "TCPN-024f43a0-bd46-4291-b9bd-21f6ada9c6c1";
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();

        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (coupon);
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        CouponIssue couponIssue = iCouponService.getCouponDetailsById (couponId);
        assertNotNull (couponIssue);
        assertEquals (coupon.getMemberId (), couponIssue.getMemberId ());
    }

    @Test
    public void getDetailCouponTest_Empty() {
        String couponId = "TCPN-0";
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (null);
        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (null);
        CouponIssue couponIssue = iCouponService.getCouponDetailsById (couponId);
        assertNull (couponIssue);
    }

    @Test
    public void getDetailCouponTest_Error() {
        String couponId = "TCPN-0";
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenThrow (new RuntimeException ());
        CouponIssue couponIssue = iCouponService.getCouponDetailsById (couponId);
        assertNull (couponIssue);
    }

    @Test
    public void getAllDataTest_Success() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        ArrayList<Coupon> list = new ArrayList<> ();
        list.add (coupon);
        when (couponRepoMock.findAll ()).thenReturn (list);
        List<Coupon> couponList = iCouponService.getAllCoupons ();
        assertFalse (couponList.isEmpty ());
    }

    @Test
    public void getAllDataTest_Empty() {
        when (couponRepoMock.findAll ()).thenReturn (null);
        List<Coupon> couponList = iCouponService.getAllCoupons ();
        assertTrue (couponList == null);
    }

    @Test
    public void getCouponRecommendationTest_Success() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        ArrayList<Coupon> couponList = new ArrayList<> ();
        couponList.add (coupon);
        when (couponRepoMock.getRecommendation (any (), any ()))
                .thenReturn (couponList);
        when (masterRepoMock.findAllByMCouponIdAndAndMCouponAmount (anyString (), anyLong ()))
                .thenReturn (mcoupon);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR-01");
        jsonObject.put ("amount", 25000);
        List<CouponIssue> list = iCouponService.getCouponRecommendation (jsonObject);
        assertFalse (list.isEmpty ());
        assertTrue (list.get (0).getMemberId ().equals (coupon.getMemberId ()));
    }

    @Test
    public void getCouponRecommendationTest_Empty() {
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR");
        jsonObject.put ("amount", 25000);
        when (couponRepoMock.getRecommendation (any (), any ())).thenReturn (null);
        when (masterRepoMock.findAllByMCouponIdAndAndMCouponAmount (anyString (), anyLong ()))
                .thenReturn (null);
        List<CouponIssue> list = iCouponService.getCouponRecommendation (jsonObject);
        assertTrue (list.isEmpty ());
    }

    @Test
    public void getCouponRecommendationTest_Error() {
        when (masterRepoMock.findAllByMCouponIdAndAndMCouponAmount (anyString (), anyLong ()))
                .thenThrow (new RuntimeException ());
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR");
        jsonObject.put ("amount", 25000);
        List<CouponIssue> list = iCouponService.getCouponRecommendation (jsonObject);
        assertTrue (list.isEmpty ());
    }

    @Test
    public void generateCouponTest_Success() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                .withMemberId ("MemberId")
                .withCouponName ("Coupon 2K")
                .withCouponAmount (2000L)
                .withPaymentMethod ("000")
                .withCouponStatus ("Available")
                .withCouponExpired ("2019-10-24")
                .withCouponId ("CouponId")
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        Couponhistory couponhistory = Couponhistory.builder ()
                .couponhistoryId ("historyId")
                .couponId ("couponId")
                .memberId ("memberId")
                .paymentId ("paymentId")
                .build ();
        ArrayList<Mcoupon> mcoupons = new ArrayList<> ();
        mcoupons.add (mcoupon);
        JSONObject object = new JSONObject ();
        object.put ("key", "Id");
        object.put ("value", 1);
        when (couponRepoMock.insertCoupon (anyString (), anyString (), anyString (), anyString ()))
                .thenReturn (1);
        when (masterRepoMock.checkMinimumTransaction (anyLong ())).thenReturn (mcoupons);
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (coupon);
        when (generateCouponHistoryRepoMock.save (any ())).thenReturn (couponhistory);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR-01");
        jsonObject.put ("amount", 25000);
        String response = iCouponService.saveOrUpdateCoupon (jsonObject);
        assertEquals ("Success", response);
    }

    @Test
    public void generateCouponTest_Failed() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                .withMemberId ("MemberId")
                .withCouponName ("Coupon 2K")
                .withCouponAmount (2000L)
                .withPaymentMethod ("000")
                .withCouponStatus ("Available")
                .withCouponExpired ("2019-10-24")
                .withCouponId ("CouponId")
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (2000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        Couponhistory couponhistory = Couponhistory.builder ()
                .couponhistoryId ("historyId")
                .couponId ("couponId")
                .memberId ("memberId")
                .paymentId ("paymentId")
                .build ();

        ArrayList<Mcoupon> mcoupons = new ArrayList<> ();
        mcoupons.add (mcoupon);

        when (couponRepoMock.insertCoupon (anyString (), anyString (), anyString (), anyString ()))
                .thenReturn (0);
        when (masterRepoMock.checkMinimumTransaction (anyLong ())).thenReturn (mcoupons);
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (coupon);
        when (generateCouponHistoryRepoMock.save (any ())).thenReturn (couponhistory);

        JSONObject jsonObject = new JSONObject ();
        String response = iCouponService.saveOrUpdateCoupon (jsonObject);
        assertEquals ("Failed", response);
    }

    @Test
    public void generateCouponTest_Error() {
        when (masterRepoMock.checkMinimumTransaction (anyLong ())).thenThrow (new RuntimeException ());
        JSONObject jsonObject = new JSONObject ();
        String response = iCouponService.saveOrUpdateCoupon (jsonObject);
        assertEquals ("Failed", response);
    }

    @Test
    public void updateCouponStatusTest_Success() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                .withMemberId ("MemberId")
                .withCouponName ("Coupon 12K")
                .withCouponAmount (12000L)
                .withPaymentMethod ("000")
                .withCouponStatus ("Available")
                .withCouponExpired ("2019-10-24")
                .withCouponId ("CouponId")
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (12000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        Redeemhistory redeemhistory = Redeemhistory.builder ()
                .couponId ("CouponId")
                .memberId ("memberId")
                .idRedeem ("ID")
                .paymentId ("paymentId")
                .build ();
        when (redeemHistoryRepoMock.findAllByPaymentId (anyString ())).thenReturn (null);
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (coupon);
        when (redeemHistoryRepoMock.save (any ())).thenReturn (redeemhistory);
        when (couponRepoMock.updateCouponStatus (anyString (), anyString (), any ())).thenReturn (1);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "CouponId");
        jsonObject.put ("memberId", "MemberId");
        jsonObject.put ("paymentMethodCode", "000");
        jsonObject.put ("paymentId", "cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        jsonObject.put ("couponAmount", 12000);
        int value = iCouponService.updateStatus (jsonObject);
        assertEquals (1, value);
    }

    @Test
    public void updateCouponStatusTest_Failed() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (12000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        Redeemhistory redeemhistory = Redeemhistory.builder ()
                .couponId ("CouponId")
                .memberId ("memberId")
                .idRedeem ("ID")
                .paymentId ("paymentId")
                .build ();
        when (redeemHistoryRepoMock.findAllByPaymentId (anyString ())).thenReturn (redeemhistory);
        when (masterRepoMock.findAllByMCouponId (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.findAllByCouponId (anyString ())).thenReturn (coupon);
        when (redeemHistoryRepoMock.save (any ())).thenReturn (redeemhistory);
        when (couponRepoMock.updateCouponStatus (anyString (), anyString (), any ())).thenReturn (1);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId", "USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode", "000");
        jsonObject.put ("paymentId", "cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        int value = iCouponService.updateStatus (jsonObject);
        assertEquals (0, value);
    }

    @Test
    public void updateCouponStatusTest_Error() {
        when (redeemHistoryRepoMock.findAllByPaymentId (any ()))
                .thenThrow (new RuntimeException ());
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId", "USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode", "000");
        jsonObject.put ("paymentId", "cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        int value = iCouponService.updateStatus (jsonObject);
        assertEquals (0, value);
    }

    @Test
    public void updateCouponStatusTrueTest_Success() {
        when (couponRepoMock.rollbackCouponStatus (anyString (), any ())).thenReturn (1);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "CouponId");
        int rows = iCouponService.updateStatusTrue (jsonObject);
        assertEquals (1, rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Failed() {
        when (couponRepoMock.rollbackCouponStatus (anyString (), any ()))
                .thenReturn (0);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "CouponId");
        int rows = iCouponService.updateStatusTrue (jsonObject);
        assertEquals (0, rows);
    }

    @Test
    public void updateCouponStatusTrueTest_Error() {
        when (couponRepoMock.rollbackCouponStatus (anyString (), any ()))
                .thenThrow (new RuntimeException ());
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId", "CouponId");
        int rows = iCouponService.updateStatusTrue (jsonObject);
        assertEquals (0, rows);
    }

    @Test
    public void generateNewCouponForNewMemberTest_Success() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (12000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        ArrayList<Coupon> coupons = new ArrayList<> ();
        coupons.add (coupon);

        when (masterRepoMock.findAllByMCouponDescription (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.checkExitNewMember (anyString (), anyString ()))
                .thenReturn (new ArrayList<> ());
        when (couponRepoMock.insertCoupon (anyString (), anyString (), anyString (), anyString ()))
                .thenReturn (1);

        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "New");
        jsonObject.put ("status", "new member");
        int value = iCouponService.firstCoupon (jsonObject);
        assertEquals (1, value);
    }

    @Test
    public void generateNewCouponForNewMemberTest_Failed() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponId ("Mid")
                .withMCouponDesc ("Coupon")
                .withMCouponAmount (12000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();
        ArrayList<Coupon> coupons = new ArrayList<> ();
        coupons.add (coupon);
        JSONObject object = new JSONObject ();
        object.put ("key", "Id");
        object.put ("value", 0);
        when (masterRepoMock.findAllByMCouponDescription (anyString ())).thenReturn (mcoupon);
        when (couponRepoMock.checkExitNewMember (anyString (), anyString ()))
                .thenReturn (new ArrayList<> ());
        when (couponRepoMock.insertCoupon (anyString (), anyString (), anyString (), anyString ()))
                .thenReturn (0);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR-Test");
        jsonObject.put ("status", "new member");
        int value = iCouponService.firstCoupon (jsonObject);
        assertEquals (0, value);
    }

    @Test
    public void generateNewCouponForNewMemberTest_Error() {
        when (masterRepoMock.findAllByMCouponDescription (anyString ())).thenThrow (new RuntimeException ());

        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId", "USR-Test");
        jsonObject.put ("status", "new member");
        int value = iCouponService.firstCoupon (jsonObject);
        assertEquals (0, value);
    }

    @Test
    public void checkExistTest_Success() {
        when (couponRepoMock.checkExitNewMember (anyString (), anyString ()))
                .thenReturn (new ArrayList<> ());

        List<Coupon> couponObj = iCouponService.checkForNewMember ("USR-11A", "MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertTrue (couponObj.isEmpty ());
    }

    @Test
    public void checkExistTest_Failed() {
        Coupon coupon = Coupon.builder ()
                .couponId ("CouponId")
                .mCouponId ("Mid")
                .memberId ("USRid")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        ArrayList<Coupon> coupons = new ArrayList<> ();
        coupons.add (coupon);
        when (couponRepoMock.checkExitNewMember (anyString (), anyString ()))
                .thenReturn (coupons);

        List<Coupon> couponObj = iCouponService.checkForNewMember ("USR-03", "MCPN-e4770f4c-0d5d-4abe-a508-f666721abce9");
        assertFalse (couponObj.isEmpty ());
    }

    @Test
    public void deleteCouponTest_Success() {
        when (couponRepoMock.deleteAllByCouponId (anyString ())).thenReturn (1);
        int value = iCouponService.deleteById ("CouponId");
        assertTrue (value == 1);
    }

    @Test
    public void deleteCouponTest_Failed() {
        when (couponRepoMock.deleteAllByCouponId (anyString ())).thenReturn (0);
        int value = iCouponService.deleteById ("CouponId");
        assertTrue (value == 0);
    }
}