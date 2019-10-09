package com.danapprentech.promotion.test.repository;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.IMasterCouponRepository;
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
public class MasterCouponRepositoryTest {
    @Autowired
    private IMasterCouponRepository iMasterCouponRepository;

    @Test
    public void getDetailTest_Success(){
        String id = "MCPN-33640c9c-ec39-4b73-81a4-480a6369a6e4";
        Mcoupon mcoupon = iMasterCouponRepository.getDetailById(id);
        assertNotNull (mcoupon);
    }
    @Test
    public void getDetailTest_Empty(){
        String id = "ID";
        Mcoupon mcoupon = iMasterCouponRepository.getDetailById(id);
        assertNull (mcoupon);
    }
    @Test
    public void getDataTest_Success(){
        String id = "MCPN-33640c9c-ec39-4b73-81a4-480a6369a6e4";
        Long amount = 50000L;
        Mcoupon mcoupon = iMasterCouponRepository.getAllById (id,amount);
        assertNotNull (mcoupon);
    }
    @Test
    public void getDataTest_Empty(){
        String id = "MCPN-33640c9c-ec39-4b73-81a4-480a6369a6e4";
        Long amount = 1000L;
        Mcoupon mcoupon = iMasterCouponRepository.getAllById (id,amount);
        assertNull (mcoupon);
    }
}
