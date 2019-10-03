package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ApiModel(description = "Update Coupon Status History")
public class Couponhistory {
    @Id
    private String couponhistoryId;
    private String paymentId;
    private String couponId;
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
