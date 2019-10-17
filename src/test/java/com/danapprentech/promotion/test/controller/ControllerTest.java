package com.danapprentech.promotion.test.controller;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.BaseResponse;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest extends AbstractTest{

    @MockBean
    private ICouponService iCouponService;
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void getCouponListTest_Success() throws Exception {
        Coupon coupon = Coupon.builder ()
                .couponId ("CPN-01")
                .memberId ("USR-01")
                .couponExpired ("2019-10-24")
                .couponStatus ("available")
                .createTime (LocalDateTime.now ())
                .mCouponId ("MCPN-01")
                .updateTime (LocalDateTime.now ().plusDays (1))
                .build ();
        ArrayList<Coupon> couponList = new ArrayList<> ();
        couponList.add (coupon);

        when(iCouponService.getAllCoupons ()).thenReturn(couponList);

        String inputJson = super.mapToJson (coupon);
        String url = "/promotion/all";
        MvcResult mvcResult = mvc.perform (MockMvcRequestBuilders.get (url)
                .accept (MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn ();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Coupon[] couponsList = super.mapFromJson(content, Coupon[].class);
        assertTrue (couponsList[0].getMCouponId ().equals ("MCPN-01"));
    }

    @Test
    public void getCouponListTest_Empty() throws Exception {

        when(iCouponService.getAllCoupons ()).thenReturn(null);

        String inputJson = super.mapToJson (null);
        String url = "/promotion/all";
        MvcResult mvcResult = mvc.perform (MockMvcRequestBuilders.get (url)
                .accept (MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn ();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        assertTrue(mvcResult.getResponse().getContentAsString().isEmpty ());
    }

    @Test
    public void getCouponDetailTest_Success() throws Exception {
        CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                .withCouponId ("CPN-01")
                .withPaymentMethod ("000")
                .withMemberId ("USR-1")
                .withCouponAmount (2000L)
                .withCouponExpired ("2019-10-24")
                .withCouponName ("2K")
                .withCouponStatus ("available")
                .build ();
        String url = "/promotion/detail/TCPN-07716c66-7fd5-45a3-8e98-44d1c79590a4";
        when (iCouponService.getCouponDetailsById (anyString ())).thenReturn (couponIssue);
        String inputJson = super.mapToJson (couponIssue);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        ObjectMapper mapper = new ObjectMapper ();
        CouponIssue coupon= mapper.convertValue (response.getData (),CouponIssue.class);

        assertTrue(couponIssue.getMemberId ().equalsIgnoreCase ("USR-1"));
    }

    @Test
    public void getCouponDetailTest_Empty() throws Exception {
        String couponId ="TCPN-011107e4-9813-41bf-a406-4df49e9ca18b";
        String url = "/promotion/detail/"+couponId;
        when (iCouponService.getCouponDetailsById (anyString ())).thenReturn (null);
        String inputJson = super.mapToJson (null);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        assertTrue(response.getMessage ().equalsIgnoreCase ("Coupon not found for this id :: "+couponId));
    }

    @Test
    public void getCouponRecommendationTest_Success() throws Exception {
        String url = "/promotion/recommended";
        CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                .withCouponId ("CPN-01")
                .withPaymentMethod ("000")
                .withMemberId ("USR-1")
                .withCouponAmount (2000L)
                .withCouponExpired ("2019-10-24")
                .withCouponName ("2K")
                .withCouponStatus ("available")
                .build ();
        ArrayList<CouponIssue> couponIssues = new ArrayList<> ();
        couponIssues.add (couponIssue);
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-02");
        jsonObject.put ("amount",25000);

        when (iCouponService.getCouponRecommendation (jsonObject)).thenReturn (couponIssues);
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        ObjectMapper mapper = new ObjectMapper ();
        CouponIssue[] list= mapper.convertValue (response.getData (),CouponIssue[].class);
        assertTrue(list[0].getMemberId ().equals ("USR-1"));
    }

    @Test
    public void getCouponRecommendationTest_Empty() throws Exception {
        String url = "/promotion/recommended";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-88");
        jsonObject.put ("amount",25000);

        when (iCouponService.getCouponRecommendation (jsonObject)).thenReturn (null);
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        ObjectMapper mapper = new ObjectMapper ();
        CouponIssue[] couponIssue= mapper.convertValue (response.getData (),CouponIssue[].class);
        assertTrue(couponIssue == null);
    }

    @Test
    public void getCouponRecommendationTest_Error() throws Exception {
        String url = "/promotion/recommended";
        String inputJson = super.mapToJson (null);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void createNewCouponTest_Success() throws Exception {
        String url = "/promotion/create/coupon";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-02");
        jsonObject.put ("amount",25000);
        String expected ="Success";
        when (iCouponService.saveOrUpdateCoupon (jsonObject)).thenReturn ("Success");
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value (), status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        assertTrue(expected.equalsIgnoreCase (response.getMessage ()));
    }

    @Test
    public void createNewCouponTest_Failed() throws Exception {
        String url = "/promotion/create/coupon";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-08");

        when (iCouponService.saveOrUpdateCoupon (jsonObject)).thenReturn ("Failed");
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(HttpStatus.OK.value (), status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        assertTrue(response.getMessage ().equalsIgnoreCase ("Failed"));
    }

    @Test
    public void createNewCouponTest_Error() throws Exception {
        String url = "/promotion/create/coupon";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void updateCouponStatusTest_Success() throws Exception {
        String url = "/promotion/update/coupon";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","A1");
        jsonObject.put ("memberId","A1");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","A1");
        jsonObject.put ("couponAmount",12000);

        when (iCouponService.updateStatus (jsonObject)).thenReturn (1);
        String inputJson = super.mapToJson(jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        assertEquals("Succeed",response.getMessage ());
    }

    @Test
    public void updateCouponStatusTest_Failed() throws Exception {
        String url = "/promotion/update/coupon";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("couponId","TCPN-1094c2c6-499a-48c9-b3c0-c269a71a10a3");
        jsonObject.put ("memberId","USR-994facc9-2e40-47c2-840e-77680a90e033");
        jsonObject.put ("paymentMethodCode","000");
        jsonObject.put ("paymentId","cf7ec9ed-0614-49c2-bec9-ed0614b9c275");
        when (iCouponService.updateStatus (jsonObject)).thenReturn (0);
        String inputJson = super.mapToJson(jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        assertEquals ("Failed",response.getMessage ());
    }

    @Test
    public void updateCouponStatusTest_Error() throws Exception {
        String url = "/promotion/update/coupon";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

    @Test
    public void createCouponNewMember_Success() throws Exception {
        String url = "/promotion/create/coupon/first";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-New");
        jsonObject.put ("status","new member");
        when (iCouponService.firstCoupon (jsonObject)).thenReturn (1);
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        assertTrue (response.getMessage ().equalsIgnoreCase ("Success"));
    }

    @Test
    public void createCouponNewMember_Failed() throws Exception {
        String url = "/promotion/create/coupon/first";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("memberId","USR-New");
        jsonObject.put ("status","new member");
        when (iCouponService.firstCoupon (jsonObject)).thenReturn (0);
        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content (inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        assertTrue(response.getMessage ().equalsIgnoreCase ("Failed to save data"));
    }

    @Test
    public void createCouponNewMember_Error() throws Exception {
        String url = "/promotion/create/coupon/first";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(500, status);
    }

}
