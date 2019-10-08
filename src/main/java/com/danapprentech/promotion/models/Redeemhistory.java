package com.danapprentech.promotion.models;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@Builder
@ApiModel(description = "Redeem History")
public class Redeemhistory {
    @Id
    private String id_redeem;
    private String payment_id;
    private String coupon_id;
    private String member_id;

}
