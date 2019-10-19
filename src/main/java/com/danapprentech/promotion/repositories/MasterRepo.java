package com.danapprentech.promotion.repositories;

import com.danapprentech.promotion.models.Mcoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MasterRepo extends JpaRepository<Mcoupon,String> {
    @Modifying
    @Query(value = "INSERT INTO Mcoupon (m_coupon_id,m_coupon_description, " +
            "m_coupon_amount, m_minimum_transaction,payment_method)" +
            "VALUES (:masterId,:desc,:amount,:minTrans,:paymentMethod)", nativeQuery = true)
    int insertData(String masterId,String desc,Long amount, Long minTrans, String paymentMethod);

    @Query("FROM Mcoupon WHERE mCouponId=:master")
    Mcoupon findAllByMCouponId(String master);

    @Query("FROM Mcoupon WHERE mCouponId=:masterId AND mCouponAmount BETWEEN 0 AND :amount")
    Mcoupon findAllByMCouponIdAndAndMCouponAmount(String masterId,Long amount);

    @Query("FROM Mcoupon WHERE mMinimumTransaction<=:amount ORDER BY mMinimumTransaction DESC")
    List<Mcoupon> checkMinimumTransaction(long amount);

    @Query("FROM Mcoupon WHERE mCouponDescription=:description")
    Mcoupon findAllByMCouponDescription(String description);
}
