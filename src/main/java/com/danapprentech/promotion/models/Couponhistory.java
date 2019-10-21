package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@ApiModel(description = "Update Coupon Status History")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public String getCouponId() {
        return couponId;
    }
}
