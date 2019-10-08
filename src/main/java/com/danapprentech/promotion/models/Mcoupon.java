package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@ApiModel(description = "Master coupon")
public class Mcoupon {
    @Id
    @ApiModelProperty(notes = "Id of Coupon Master Table")
    private String mCouponId;
    @ApiModelProperty(notes = "Coupon Description")
    private String mCouponDescription;
    @ApiModelProperty(notes = "Minimum Transaction")
    private Long mMinimumTransaction;
    @ApiModelProperty(notes = "Payment Method")
    private String paymentMethod;
    @ApiModelProperty(notes = "Coupon Amount")
    private Long mCouponAmount;

    public String getmCouponId() {
        return mCouponId;
    }

    public String getmCouponDescription() {
        return mCouponDescription;
    }

    public Long getmMinimumTransaction() {
        return mMinimumTransaction;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Long getmCouponAmount() {
        return mCouponAmount;
    }

    public static final class MasterCouponBuilder{
        private String mCouponId;
        private String mCouponDescription;
        private Long mMinimumTransaction;
        private String paymentMethod;
        private Long mCouponAmount;

        public MasterCouponBuilder withMCouponId(String mCouponId){
            this.mCouponId = mCouponId;
            return this;
        }
        public MasterCouponBuilder withMCouponDesc(String mCouponDescription){
            this.mCouponDescription = mCouponDescription;
            return this;
        }
        public MasterCouponBuilder withMCouponMinTransaction(Long mCouponMinTransaction){
            this.mMinimumTransaction = mCouponMinTransaction;
            return this;
        }
        public MasterCouponBuilder withPaymentMethod(String paymentMethod){
            this.paymentMethod = paymentMethod;
            return this;
        }
        public MasterCouponBuilder withMCouponAmount(Long mCouponAmount){
            this.mCouponAmount = mCouponAmount;
            return this;
        }

        public Mcoupon build(){
            Mcoupon mcoupon = new Mcoupon ();
            mcoupon.mCouponId = this.mCouponId;
            mcoupon.mCouponDescription = this.mCouponDescription;
            mcoupon.mMinimumTransaction = this.mMinimumTransaction;
            mcoupon.paymentMethod = this.paymentMethod;
            mcoupon.mCouponAmount = this.mCouponAmount;
            return mcoupon;
        }

    }
}
