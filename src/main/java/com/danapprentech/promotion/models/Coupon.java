package com.danapprentech.promotion.models;

import com.danapprentech.promotion.helper.ParseDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ParseDeserializer.class)
    private LocalDateTime createTime;
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = ParseDeserializer.class)
    private LocalDateTime updateTime;

}
