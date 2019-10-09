package com.danapprentech.promotion.test.controller;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class McouponControllerTest extends AbstractTest{
    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void addDataMasterTest_Success() throws Exception {
        String uri = "/master/add";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put ("m_coupon_description","17 Agustus");
        jsonObject.put ("m_minimum_transaction",0L);
        jsonObject.put ("m_coupon_amount",12000);
        jsonObject.put ("paymentMethod","000");

        String inputJson = super.mapToJson (jsonObject);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post (uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        assertEquals ("Save data success",response.getData ());
    }

    @Test
    public void addDataMasterTest_Failed() throws Exception {
        String uri = "/master/add";
        JSONObject jsonObject = new JSONObject ();
        jsonObject.put ("m_coupon_description","16 Agustus");
        jsonObject.put ("m_minimum_transaction",0L);
        jsonObject.put ("m_coupon_amount",12000);

        String inputJson = super.mapToJson (jsonObject);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post (uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);

        assertEquals ("Save data failed",response.getData ());
    }

    @Test
    public void addDataMasterTest_Error() throws Exception {
        String uri = "/master/add";
        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponDesc ("17 Agustus")
                .withMCouponAmount (10000L)
                .withMCouponMinTransaction (0L)
                .withPaymentMethod ("000")
                .build ();

        String inputJson = super.mapToJson (mcoupon);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post (uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        BaseResponse response = super.mapFromJson(content, BaseResponse.class);
        assertEquals (400,response.getCode ());
        assertEquals ("error",response.getData ());
    }
}
