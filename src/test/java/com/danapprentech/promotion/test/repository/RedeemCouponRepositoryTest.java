package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@Transactional
@SpringBootTest
public class RedeemCouponRepositoryTest {
    @Autowired
    private IRedeemHistoryRepository iRedeemHistoryRepository;

    @Test
    public void getHistoryByPaymentIdTest_Success(){
        String paymentId = "cf7ec9ed-0614-49c2-bec9-ed0614b9c275";
        Redeemhistory redeemhistory = iRedeemHistoryRepository.getRedeemHistoryByPaymentId (paymentId);
        assertNotNull (redeemhistory);
    }

    @Test
    public void getHistoryByPaymentIdTest_Empty(){
        String paymentId = "cf7ec9ed-0614-49c2-bec9-ed0614b9c275";
        Redeemhistory redeemhistory = iRedeemHistoryRepository.getRedeemHistoryByPaymentId (paymentId);
        assertNull (redeemhistory);
    }
}
