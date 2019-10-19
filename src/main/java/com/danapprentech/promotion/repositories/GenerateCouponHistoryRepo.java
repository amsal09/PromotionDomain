package com.danapprentech.promotion.repositories;

import com.danapprentech.promotion.models.Couponhistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GenerateCouponHistoryRepo extends JpaRepository<Couponhistory,String> {
    @Query("FROM Couponhistory WHERE paymentId=:paymentId")
    Couponhistory findAllByPaymentId(String paymentId);

}
