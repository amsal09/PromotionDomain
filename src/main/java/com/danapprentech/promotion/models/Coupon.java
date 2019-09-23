package com.danapprentech.promotion.models;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Coupon {

    @Id
    int couponId;
    int memberId;
    String couponName;
    String couponAmount;
    String couponExpired;
    String couponStatus;

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
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
