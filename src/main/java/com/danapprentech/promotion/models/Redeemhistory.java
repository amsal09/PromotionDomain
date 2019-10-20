package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Redeem History")
@Entity
public class Redeemhistory {
    @Id
    private String idRedeem;
    @ApiModelProperty(notes = "Payment Id")
    private String paymentId;
    @ApiModelProperty(notes = "Coupon Id")
    private String couponId;
    @ApiModelProperty(notes = "Member Id")
    private String memberId;

}
