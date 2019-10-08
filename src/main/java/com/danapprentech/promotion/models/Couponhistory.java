package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ApiModel(description = "Update Coupon Status History")
public class Couponhistory {
    @Id
    @ApiModelProperty(notes = "Id of coupon history Table")
    private String couponhistoryId;
    @ApiModelProperty(notes = "Payment Id")
    private String paymentId;
    @ApiModelProperty(notes = "Coupon Id")
    private String couponId;
    @ApiModelProperty(notes = "Member Id")
    private String memberId;

    public String getCouponhistoryId() {
        return couponhistoryId;
    }

    public void setCouponhistoryId(String couponhistoryId) {
        this.couponhistoryId = couponhistoryId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
}
