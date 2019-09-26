package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@ApiModel(description = "All details about coupon")
public class Coupon {

    @ApiModelProperty(notes = "Coupon ID")
    private String couponId;
    @ApiModelProperty(notes = "Member phone number")
    private String memberPhone;
    @ApiModelProperty(notes = "Coupon name")
    private String couponName;
    @ApiModelProperty(notes = "Coupon amount")
    private Long couponAmount;
    @ApiModelProperty(notes = "Coupon expired")
    private String couponExpired;
    @ApiModelProperty(notes = "Coupon status")
    private String couponStatus;

    @Id

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Long getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(Long couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponExpired() {
        return couponExpired;
    }

    public void setCouponExpired(String couponExpired) {
        this.couponExpired = couponExpired;
    }

    public String getCouponStatus() {
        return couponStatus;
    }

    public void setCouponStatus(String couponStatus) {
        this.couponStatus = couponStatus;
    }
}
