package com.danapprentech.promotion.repositories;

import com.danapprentech.promotion.models.Redeemhistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RedeemHistoryRepo extends JpaRepository<Redeemhistory,String> {
    @Query("FROM Redeemhistory WHERE paymentId=:paymentId")
    Redeemhistory findAllByPaymentId(String paymentId);

}
