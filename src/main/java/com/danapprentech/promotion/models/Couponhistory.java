package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@ApiModel(description = "Update Coupon Status History")
@Entity
public class Couponhistory {
    @ApiModelProperty(notes = "Id of coupon history Table")
    @Id
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
