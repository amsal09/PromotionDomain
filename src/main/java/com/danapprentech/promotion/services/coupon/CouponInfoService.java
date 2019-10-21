package com.danapprentech.promotion.services.coupon;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.CouponRepo;
import com.danapprentech.promotion.repositories.MasterRepo;
import com.danapprentech.promotion.response.CouponDetail;
import com.danapprentech.promotion.response.CouponIssue;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional
public class CouponInfoService {
    private static final Logger logger = LoggerFactory.getLogger(CouponInfoService.class);
    @Autowired
    private CouponRepo couponRepo;
    @Autowired
    private MasterRepo masterRepo;

    @Transactional
    public CouponIssue getCouponDetailsById(String couponID) {
        return getCouponIssue (couponID, couponRepo, masterRepo);
    }

    public static CouponIssue getCouponIssue(String couponID, CouponRepo couponRepo, MasterRepo masterRepo) {
        CouponIssue couponIssue = null;
        try {
            Coupon coupon = couponRepo.findAllByCouponId (couponID);
            Mcoupon mcoupon = masterRepo.findAllByMCouponId (coupon.getMCouponId ());
            couponIssue = new CouponIssue.CouponIssuebuilder ()
                    .withCouponId (coupon.getCouponId ())
                    .withMemberId (coupon.getMemberId ())
                    .withCouponName (mcoupon.getmCouponDescription ())
                    .withCouponAmount (mcoupon.getmCouponAmount ())
                    .withPaymentMethod (mcoupon.getPaymentMethod ())
                    .withCouponStatus (coupon.getCouponStatus ())
                    .withCouponExpired (coupon.getCouponExpired ())
                    .build ();
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return couponIssue;
    }

    @Transactional
    public List<Coupon> getAllCoupons() {

        return couponRepo.findAll ();
    }

    @Transactional
    public List<CouponIssue> getCouponRecommendation(JSONObject jsonObject) {
        ArrayList<Mcoupon> mcouponList = new ArrayList<Mcoupon> ();
        ArrayList<CouponIssue> couponIssueList = new ArrayList<CouponIssue> ();
        List<CouponIssue> list = new ArrayList<CouponIssue> ();
        try {
            String time = new SimpleDateFormat ("yyyy-MM-dd").format(new Date ());
            String memberId = (String)jsonObject.get ("memberId");
            List<Coupon> couponList = couponRepo.getRecommendation (memberId,time);

            Number number = (Number) jsonObject.get ("amount");
            Long value = number.longValue ();

            for (Coupon coupon: couponList) {
                mcouponList.add (masterRepo.findAllByMCouponIdAndAndMCouponAmount (coupon.getMCouponId (),value));
            }
            for(int i=0; i<couponList.size (); i++){
                CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                        .withCouponId (couponList.get (i).getCouponId ())
                        .withMemberId (couponList.get (i).getMemberId ())
                        .withCouponAmount (mcouponList.get (i).getmCouponAmount ())
                        .withCouponName (mcouponList.get (i).getmCouponDescription ())
                        .withPaymentMethod (mcouponList.get (i).getPaymentMethod ())
                        .withCouponExpired (couponList.get (i).getCouponExpired ())
                        .withCouponStatus (couponList.get (i).getCouponStatus ())
                        .build ();

                couponIssueList.add (couponIssue);
            }
            Collections.sort (couponIssueList, new Comparator<CouponIssue> () {
                @Override
                public int compare(CouponIssue o1, CouponIssue o2) {
                    return (int) (o1.getCouponAmount () - o2.getCouponAmount ());
                }
            });
            Collections.reverse (couponIssueList);
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return couponIssueList;
    }

    public CouponDetail getCouponDetailCoupon(String couponID) {
        CouponDetail couponDetail = null;
        try {
            Coupon coupon = couponRepo.findAllByCouponId (couponID);
            Mcoupon mcoupon = masterRepo.findAllByMCouponId (coupon.getMCouponId ());
            couponDetail = CouponDetail.builder ()
                    .couponId (coupon.getCouponId ())
                    .memberId (coupon.getMemberId ())
                    .couponName (mcoupon.getmCouponDescription ())
                    .couponAmount (mcoupon.getmCouponAmount ())
                    .couponExpired (coupon.getCouponExpired ())
                    .couponStatus (coupon.getCouponStatus ())
                    .paymentMethod (mcoupon.getPaymentMethod ())
                    .createdAt (coupon.getCreateTime ())
                    .updatedAt (coupon.getUpdateTime ())
                    .build ();
        }catch (Exception e){
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        return couponDetail;
    }
}
