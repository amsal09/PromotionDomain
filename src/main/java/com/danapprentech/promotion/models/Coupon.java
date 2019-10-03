package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ApiModel(description = "All details about coupon")
public class Coupon {

    @ApiModelProperty(notes = "Coupon ID")
    @Id
    private String couponId;
    @ApiModelProperty(notes = "Coupon Id of Coupon Master Table")
    private String mCouponId;
    @ApiModelProperty(notes = "Member id")
    private String memberId;
    @ApiModelProperty(notes = "Coupon expired")
    private String couponExpired;
    @ApiModelProperty(notes = "Coupon status")
    private String couponStatus;

    public String getCouponId() {
        return couponId;
    }

    public String getmCouponId() {
        return mCouponId;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getCouponExpired() {
        return couponExpired;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public static final class CouponBuilder{
        private String couponId;
        private String mCouponId;
        private String memberId;
        private String couponExpired;
        private String couponStatus;

        public CouponBuilder withCouponId(String couponId){
            this.couponId = couponId;
            return this;
        }
        public CouponBuilder withMasterCouponId(String mCouponId){
            this.mCouponId = mCouponId;
            return this;
        }

        public CouponBuilder withMemberId(String memberId){
            this.memberId = memberId;
            return this;
        }

        public CouponBuilder withCouponExpired(String couponExpired){
            this.couponExpired = couponExpired;
            return this;
        }
        public CouponBuilder withCouponStatus(String couponStatus){
            this.couponStatus = couponStatus;
            return this;
        }

        public Coupon build(){
            Coupon coupon = new Coupon ();
            coupon.couponId = this.couponId;
            coupon.mCouponId = this.mCouponId;
            coupon.memberId = this.memberId;
            coupon.couponExpired = this.couponExpired;
            coupon.couponStatus = this.couponStatus;
            return coupon;
        }
    }
}
