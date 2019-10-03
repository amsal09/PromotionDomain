package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.exception.ParserExeption;
import com.danapprentech.promotion.exception.ResourceNotFoundException;
import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import io.swagger.annotations.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@Api(value="Coupon Domain", description="Operation for coupon issue, coupon redeem and generate coupon based on payment amount")
public class CouponController {
    private ICouponService iCouponService;
    @Autowired
    public CouponController(ICouponService iCouponService) {

        this.iCouponService = iCouponService;
    }

    @ApiOperation(value = "View a list of available employees", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved list"),
            @ApiResponse(code = 401, message = "You are not authorized to view the resource"),
            @ApiResponse(code = 403, message = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })

    @GetMapping(value = "/all")
    public List<Coupon> getAllCoupon(){

        return iCouponService.getAllCoupons ();
    }

    @ApiOperation(value = "Get an coupon by coupon Id")
    @GetMapping(value = "/detail/{couponId}")
    public CouponIssue getCouponDetailBasedOnCouponID(@PathVariable String couponId) throws ResourceNotFoundException {
        CouponIssue coupon = iCouponService.getCouponDetailsById (couponId);
        if(coupon == null){
            throw new ResourceNotFoundException ("Coupon not found for this id :: " + couponId);
        }
        return coupon;
    }

    @ApiOperation(value = "Get all coupon recommendation based on member id")
    @PostMapping(value = "/recommended")
    public List<CouponIssue> getCouponRecommended(@RequestBody JSONObject jsonObject) {
        return iCouponService.getCouponRecommendation (jsonObject);
    }

    @ApiOperation(value = "Create coupon based on amount transaction")
    @PostMapping(value = "/create/coupon")
    public String createCoupon(@RequestBody JSONObject jsonObject){
        int rows = iCouponService.saveOrUpdateCoupon (jsonObject);
        return "Generate new coupon successfully, and save data by "+rows+" (rows)";
    }

    @ApiOperation(value = "update coupon status")
    @PutMapping("/update/coupon")
    public Boolean couponRedeem(@RequestBody JSONObject jsonObject){
        boolean isSuccess = false;

        if(iCouponService.updateStatus (jsonObject) !=0){
            isSuccess = true;
        }
        return isSuccess;
    }

    @ApiOperation(value = "Rollback status coupon to be true")
    @PutMapping("/update/coupon/true")
    public String updateCouponStatusTrue(@RequestBody JSONObject jsonObject){
        String response = "failed";

        if(iCouponService.updateStatusTrue (jsonObject) !=0){
            response="rollback coupon status successfully";
        }
        return response;
    }

    @ApiOperation(value = "Create new coupon for new member")
    @PostMapping(value = "/create/coupon/first")
    public String createCouponForNewMember(@RequestBody String body) throws ParserExeption {
        JSONParser parser = new JSONParser ();
        JSONObject jsonObject;
        String response="failed";
        try {
            jsonObject = (JSONObject) parser.parse (body);
        }catch (Exception e){
            throw new ParserExeption ("Failed to parse string to JSON");
        }
        int rows = iCouponService.firstCoupon (jsonObject);
        if(rows !=0){
            response = "Generate new coupon successfully, and save data by "+rows+" (rows)";
        }
        return response;
    }
}
