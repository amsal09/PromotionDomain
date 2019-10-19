package com.danapprentech.promotion.repositories;

import com.danapprentech.promotion.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, String> {
    @Query("FROM Coupon")
    List<Coupon> findAll();

    @Query("FROM Coupon WHERE couponId=:couponId")
    Coupon findAllByCouponId(String couponId);

    @Query("FROM Coupon WHERE memberId=:memberId AND couponExpired<=:time AND couponStatus='available'")
    List<Coupon> getRecommendation(String memberId, String time);

    @Modifying
    @Query(value = "INSERT INTO Coupon (coupon_id,m_coupon_id,member_id," +
            "coupon_expired, coupon_status) VALUES (:couponId,:masterId,:memberId,:couponExpired,'available')", nativeQuery = true)
    int insertCoupon(String couponId, String masterId, String memberId, String couponExpired);

    @Modifying
    @Query(value = "UPDATE Coupon SET couponStatus='not available', updateTime=:time " +
            "WHERE couponId=:couponId AND couponStatus='available' AND memberId=:memberId")
    int updateCouponStatus(String couponId, String memberId, LocalDateTime time);
    @Modifying
    @Query(value = "UPDATE Coupon SET couponStatus='available', updateTime=:time WHERE couponStatus='not available'" +
            "AND couponId=:couponId")
    int rollbackCouponStatus(String couponId, LocalDateTime time);

    @Query("FROM Coupon WHERE memberId=:memberId AND m_coupon_id=:masterId")
    List<Coupon> checkExitNewMember(String memberId, String masterId);

    int deleteAllByCouponId(String couponId);
}
