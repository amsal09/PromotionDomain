package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "master")
public class MasterCouponController {

    @Autowired
    private IMasterCouponService iMasterCouponService;

    @PostMapping(value = "/add")
    public String addMasterCoupon(@RequestBody JSONObject jsonObject){
        Number number1 = (Number) jsonObject.get ("m_coupon_amount");
        Long value1 = number1.longValue ();
        Number number2 = (Number) jsonObject.get ("m_minimum_transaction");
        Long value2 = number2.longValue ();

        Mcoupon mcoupon = new Mcoupon.MasterCouponBuilder ()
                .withMCouponAmount (value1)
                .withMCouponDesc ((String) jsonObject.get ("m_coupon_description"))
                .withMCouponMinTransaction (value2)
                .build ();

        int saveRow= iMasterCouponService.saveOrUpdate (mcoupon);
        return "Add data "+saveRow+" (rows) successfully";
    }
}
