package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import com.danapprentech.promotion.test.controller.AbstractTest;
import org.junit.Before;
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
public class RedeemCouponRepositoryTest extends AbstractTest {
    @Before
    public void setUp() {
        super.setUp();
    }
    @Autowired
    private IRedeemHistoryRepository iRedeemHistoryRepository;

    @Test
    public void getHistoryByPaymentIdTest_Success(){
        String paymentId = "1";
        Redeemhistory redeemhistory = iRedeemHistoryRepository.getRedeemHistoryByPaymentId (paymentId);
        assertNotNull (redeemhistory);
    }

    @Test
    public void getHistoryByPaymentIdTest_Empty(){
        String paymentId = "ZZ";
        Redeemhistory redeemhistory = iRedeemHistoryRepository.getRedeemHistoryByPaymentId (paymentId);
        assertNull (redeemhistory);
    }
}
