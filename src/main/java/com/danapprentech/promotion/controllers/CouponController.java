package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.exception.ResourceNotFoundException;
import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import io.swagger.annotations.*;
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

    @ApiOperation(value = "Get an coupon by coupon name")
    @GetMapping(value = "/detail/coupon/{couponName}")
    public List<Coupon> getCouponDetailBasedOnCouponName(@PathVariable String couponName) throws ResourceNotFoundException{
        List<Coupon> couponList = iCouponService.getCouponDetailsByName (couponName);
        if (couponList.isEmpty ()){
            throw new ResourceNotFoundException ("Coupon not found for this name :: " + couponName);
        }
         return couponList;

    }
    @ApiOperation(value = "Get an coupon by coupon Id")
    @GetMapping(value = "/detail/{couponId}")
    public Coupon getCouponDetailBasedOnCouponID(@PathVariable String couponId) throws ResourceNotFoundException {
        Coupon coupon = iCouponService.getCouponDetailsById (couponId);
        if(coupon == null){
            throw new ResourceNotFoundException ("Coupon not found for this id :: " + couponId);
        }
        return coupon;
    }

    @ApiOperation(value = "Get all coupon recommendation based on member id")
    @PostMapping(value = "/recommended")
    public List<Coupon> getCouponRecommended(@RequestBody Coupon coupon){
        return iCouponService.getCouponRecommendation (coupon.getMemberPhone (), coupon.getCouponAmount ());
    }

    @ApiOperation(value = "Update coupon status")
    @PutMapping("/update/coupon")
    public Boolean couponRedeem(@RequestBody Coupon coupon){
        boolean isSuccess = false;
        if(iCouponService.saveOrUpdateCoupon (coupon.getMemberPhone (),coupon.getCouponAmount ()) != 0){
            isSuccess = true;
        }
        if(iCouponService.updateStatus (coupon.getCouponId ())!=0){
            isSuccess = true;
        }
        return isSuccess;
    }
}
