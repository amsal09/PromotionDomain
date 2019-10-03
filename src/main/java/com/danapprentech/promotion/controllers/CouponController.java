package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.exception.ParserExeption;
import com.danapprentech.promotion.exception.ResourceNotFoundException;
import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.BaseResponse;
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

    @ApiOperation(value = "View a list of available coupon", response = List.class)
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
    public BaseResponse getCouponDetailBasedOnCouponID(@PathVariable String couponId) {
        CouponIssue coupon = null;
        BaseResponse baseResponse=null;
        try {
            coupon = iCouponService.getCouponDetailsById (couponId);
            if(coupon!=null){
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Success")
                        .withData (coupon)
                        .build ();
            }else {
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Coupon not found for this id :: " + couponId)
                        .withData (coupon)
                        .build ();
            }
        }catch (Exception e){
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (400)
                    .withMessage (e.getMessage ())
                    .withData ("Coupon not found for this id :: " + couponId)
                    .build ();
        }
        return baseResponse;
    }

    @ApiOperation(value = "Get all coupon recommendation based on member id")
    @PostMapping(value = "/recommended")
    public BaseResponse getCouponRecommended(@RequestBody JSONObject jsonObject) {
        List<CouponIssue> couponList =null;
        BaseResponse baseResponse = null;
        try {
            couponList = iCouponService.getCouponRecommendation (jsonObject);
            if(couponList!=null){
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Success")
                        .withData (couponList)
                        .build ();
            }else {
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("No coupon available for this member")
                        .withData (couponList)
                        .build ();
            }
        }catch (Exception e){
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (400)
                    .withMessage (e.getMessage ())
                    .withData (couponList)
                    .build ();
        }
        return baseResponse;
    }

    @ApiOperation(value = "Create coupon based on amount transaction")
    @PostMapping(value = "/create/coupon")
    public BaseResponse createCoupon(@RequestBody JSONObject jsonObject){
        BaseResponse baseResponse = null;
        try {
            int rows = iCouponService.saveOrUpdateCoupon (jsonObject);
            if(rows!=0){
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Success")
                        .withData ("Generate new coupon successfully, and save data by "+rows+" (rows)")
                        .build ();
            }else {
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Failed to generate coupon")
                        .withData ("null")
                        .build ();
            }
        }catch (Exception e){
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (400)
                    .withMessage (e.getMessage ())
                    .withData ("null")
                    .build ();
        }

        return baseResponse;
    }

    @ApiOperation(value = "update coupon status")
    @PutMapping("/update/coupon")
    public BaseResponse couponRedeem(@RequestBody JSONObject jsonObject){
        BaseResponse baseResponse = null;
        CouponIssue couponIssue =null;
        try {
            couponIssue = iCouponService.updateStatus (jsonObject);
            if(couponIssue!=null){
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("Success")
                        .withData (couponIssue)
                        .build ();
            }else {
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (200)
                        .withMessage ("No coupon available for this member")
                        .withData (couponIssue)
                        .build ();
            }
        }catch (Exception e){
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (400)
                    .withMessage (e.getMessage ())
                    .withData (couponIssue)
                    .build ();
        }
        return baseResponse;
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
