package com.danapprentech.promotion.response;

import org.springframework.stereotype.Component;

public class CouponIssue{

    private String couponId;
    private String memberId;
    private String couponName;
    private Long couponAmount;
    private String paymentMethod;
    private String couponExpired;
    private String couponStatus;

    public String getCouponId() {
        return couponId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getCouponName() {
        return couponName;
    }

    public Long getCouponAmount() {
        return couponAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
    public String getCouponExpired() {
        return couponExpired;
    }

    public String getCouponStatus() {
        return couponStatus;
    }


    public static final class CouponIssuebuilder{
        private String couponId;
        private String memberId;
        private String couponName;
        private Long couponAmount;
        private String paymentMethod;
        private String couponExpired;
        private String couponStatus;

        public CouponIssuebuilder withCouponId(String couponId){
            this.couponId=couponId;
            return this;
        }
        public CouponIssuebuilder withMemberId(String memberId){
            this.memberId=memberId;
            return this;
        }
        public CouponIssuebuilder withCouponName(String couponName){
            this.couponName=couponName;
            return this;
        }
        public CouponIssuebuilder withCouponAmount(Long couponAmount){
            this.couponAmount=couponAmount;
            return this;
        }
        public CouponIssuebuilder withPaymentMethod(String paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }
        public CouponIssuebuilder withCouponExpired(String couponExpired){
            this.couponExpired=couponExpired;
            return this;
        }
        public CouponIssuebuilder withCouponStatus(String couponStatus){
            this.couponStatus=couponStatus;
            return this;
        }

        public CouponIssue build(){
            CouponIssue couponIssue = new CouponIssue ();
            couponIssue.couponId=this.couponId;
            couponIssue.memberId=this.memberId;
            couponIssue.couponName=this.couponName;
            couponIssue.couponAmount=this.couponAmount;
            couponIssue.paymentMethod = this.paymentMethod;
            couponIssue.couponExpired=this.couponExpired;
            couponIssue.couponStatus=this.couponStatus;
            return couponIssue;
        }
    }

}
