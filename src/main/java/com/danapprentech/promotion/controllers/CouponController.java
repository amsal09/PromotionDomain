package com.danapprentech.promotion.controllers;

import com.danapprentech.promotion.exception.ParserExeption;
import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.response.BaseResponse;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponService;
import io.swagger.annotations.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/promotion")
@Api(value="Coupon Domain", description="Operation for coupon issue, coupon redeem and generate coupon based on payment amount")
public class CouponController {
    private static final Logger logger = LoggerFactory.getLogger(CouponController.class);
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
        logger.info ("get all coupon");
        return iCouponService.getAllCoupons ();
    }

    @ApiOperation(value = "Get an coupon by coupon Id")
    @GetMapping(value = "/detail/{couponId}")
    public BaseResponse getCouponDetailBasedOnCouponID(@PathVariable String couponId) {
        CouponIssue coupon = null;
        BaseResponse baseResponse=null;
        try {
            logger.info ("try to get detail coupon");
            coupon = iCouponService.getCouponDetailsById (couponId);
            if(coupon!=null){
                logger.info ("coupon exist with coupon id: {}", couponId);
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData (coupon)
                        .build ();
            }else {
                logger.info ("coupon does not exist with coupon id: {}", couponId);
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Coupon not found for this id :: " + couponId)
                        .withData (coupon)
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {} with coupon id: {}",e.getMessage (), couponId);
            logger.warn ("{}"+e.getStackTrace ());
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (HttpStatus.BAD_REQUEST.value ())
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
            logger.info ("try to get coupon recommendation");
            couponList = iCouponService.getCouponRecommendation (jsonObject);
            if(!couponList.isEmpty ()){
                logger.info ("Get coupon recommendation success with member id: {}", jsonObject.get ("memberId"));
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData (couponList)
                        .build ();
            }else {
                logger.info ("Get coupon recommendation failed with member id: {}", jsonObject.get ("memberId"));

                baseResponse = new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.NOT_FOUND.value ())
                        .withMessage ("No coupon available for this member")
                        .withData (couponList)
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {} when to get coupon recommendation",e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (HttpStatus.BAD_REQUEST.value ())
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
            String response = iCouponService.saveOrUpdateCoupon (jsonObject);
            if(response.equalsIgnoreCase ("success")){
                System.out.println ("create coupon success");
                logger.info ("Created coupon success");
                JSONObject json = new JSONObject ();
                json.put ("paymentId",jsonObject.get ("paymentId"));
                json.put ("field","PROMOTION");

                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData (json)
                        .build ();
            }else {
                logger.info ("Created coupon failed");
                JSONObject json = new JSONObject ();
                json.put ("paymentId",jsonObject.get ("paymentId"));
                json.put ("field","PROMOTION");
                baseResponse= new BaseResponse.BaseResponseBuilder()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Failed")
                        .withData (json)
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {} when to create new coupon",e.getMessage ());
            logger.warn ("{}"+e.getStackTrace ());

            JSONObject json = new JSONObject ();
            json.put ("paymentId",jsonObject.get ("paymentId"));
            json.put ("field","PROMOTION");
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (HttpStatus.BAD_REQUEST.value ())
                    .withMessage (e.getMessage ())
                    .withData (json)
                    .build ();
        }
        return baseResponse;
    }

    @ApiOperation(value = "update coupon status")
    @PutMapping("/update/coupon")
    public BaseResponse couponRedeem(@RequestBody JSONObject jsonObject){
        BaseResponse baseResponse = null;
        CouponIssue couponIssue = iCouponService.getCouponDetailsById ((String)jsonObject.get ("couponId"));
        try {
            logger.info ("try to update coupon status");
            int response = iCouponService.updateStatus (jsonObject);
            if(response==1){
                logger.info ("update coupon status success");
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData (couponIssue)
                        .build ();
            }else {
                logger.info ("update coupon status failed");
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Failed")
                        .withData (couponIssue)
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {}",e.getMessage ());
            logger.warn ("Stacktrace: {}"+e.getStackTrace ());
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (HttpStatus.BAD_REQUEST.value ())
                    .withMessage (e.getMessage ())
                    .withData (couponIssue)
                    .build ();
        }
        return baseResponse;
    }

    @ApiOperation(value = "Rollback status coupon to be true")
    @PutMapping("/update/coupon/true")
    public BaseResponse updateCouponStatusTrue(@RequestBody JSONObject jsonObject){
        BaseResponse baseResponse = null;
        try{
            logger.info ("try to update coupon status to be true");
            if(iCouponService.updateStatusTrue (jsonObject) !=0){
                logger.info ("update coupon status success");
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData ("rollback coupon status successfully")
                        .build ();
            }else{
                logger.info ("update coupon status failed");
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Failed")
                        .withData ("rollback coupon status failed")
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {}",e.getMessage ());
            logger.warn ("Stacktrace: {}"+e.getStackTrace ());
            baseResponse= new BaseResponse.BaseResponseBuilder ()
                    .withCode (HttpStatus.BAD_REQUEST.value ())
                    .withMessage (e.getMessage ())
                    .withData ("null")
                    .build ();
        }
        return baseResponse;
    }

    @ApiOperation(value = "Create new coupon for new member")
    @PostMapping(value = "/create/coupon/first")
    public BaseResponse createCouponForNewMember(@RequestBody String body) throws ParserExeption {
        System.out.println (body);
        JSONParser parser = new JSONParser ();
        JSONObject jsonObject;
        String response="failed";
        BaseResponse baseResponse = null;
        try {
            logger.info ("try to update coupon status to be true");
            jsonObject = (JSONObject) parser.parse (body);
            int rows = iCouponService.firstCoupon (jsonObject);
            if(rows !=0){
                logger.info ("generate new coupon for new member success");
                response = "Generate new coupon successfully, and save data by "+rows+" (rows)";
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Success")
                        .withData (response)
                        .build ();
            }else{
                logger.info ("generate new coupon for new member failed");
                baseResponse= new BaseResponse.BaseResponseBuilder ()
                        .withCode (HttpStatus.OK.value ())
                        .withMessage ("Failed to save data")
                        .withData (response)
                        .build ();
            }
        }catch (Exception e){
            logger.warn ("Error: {}",e.getMessage ());
            logger.warn ("Stacktrace: {}"+e.getStackTrace ());
            throw new ParserExeption ("Failed to parse string to JSON");
        }
        return baseResponse;
    }
}
