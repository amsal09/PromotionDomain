package com.danapprentech.promotion.rollback;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;

@Component
@Transactional
public class CouponRollback {
    @Autowired
    private EntityManagerFactory emf;

    public boolean rollbackCouponStatus(JSONObject jsonObject){
        boolean isSuccess = false;
        int updateCount = 0;
        EntityManager em = getEntityManager ();
        try {
            String couponID = (String) jsonObject.get ("couponId");
            em.getTransaction ().begin ();
            String sql = "update Coupon set coupon_status = 'available' where coupon_id = '"+couponID+"'"
                    +"and coupon_status = 'not available'";

            updateCount = em.createNativeQuery (sql)
                    .executeUpdate ();
            if(updateCount==1){
                isSuccess = true;
            }

        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        return isSuccess;
    }
    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }
}
