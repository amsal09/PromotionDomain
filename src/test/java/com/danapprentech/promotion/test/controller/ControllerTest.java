package com.danapprentech.promotion.test.controller;

import com.danapprentech.promotion.models.Coupon;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ControllerTest extends AbstractTest{
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }
    @Test
    public void getCouponList() throws Exception {
        String uri = "/promotion/all";
        MvcResult mvcResult = mvc.perform (MockMvcRequestBuilders.get (uri)
                .accept (MediaType.APPLICATION_JSON_VALUE)).andReturn ();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Coupon[] couponsList = super.mapFromJson(content, Coupon[].class);
        assertTrue(couponsList.length > 0);
    }

    @Test
    public void getCouponByName() throws Exception {
        String uri = "/promotion/detail/coupon/Coupon_5K";
        Coupon coupon = new Coupon ();
        coupon.setCouponName ("Coupon_5K");
        String inputJson = super.mapToJson (coupon);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Coupon[] couponsList = super.mapFromJson(content, Coupon[].class);
        assertTrue(couponsList.length > 0);
    }

    @Test
    public void getCouponById() throws Exception {
        String uri = "/promotion/detail/1";
        Coupon coupon = new Coupon ();
        coupon.setMemberPhone ("0986726");
        String inputJson = super.mapToJson (coupon);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Coupon coupons = super.mapFromJson(content, Coupon.class);
        assertTrue(coupons.getMemberPhone ().equals ("0986726"));
    }

    @Test
    public void getCouponRecommendation() throws Exception {
        String uri = "/promotion/recommended";
        Coupon coupon = new Coupon ();
        coupon.setMemberPhone ("0986726");
        coupon.setCouponAmount (25000L);
        String inputJson = super.mapToJson (coupon);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        Coupon[] couponsList = super.mapFromJson(content, Coupon[].class);
        assertTrue(couponsList.length > 0);
    }

}
