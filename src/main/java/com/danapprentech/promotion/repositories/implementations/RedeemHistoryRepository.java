package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import java.util.UUID;

@Repository
@EnableTransactionManagement
public class RedeemHistoryRepository implements IRedeemHistoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(RedeemHistoryRepository.class);
    @Autowired
    private EntityManagerFactory emf;

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }

    @Transactional
    @Override
    public Redeemhistory getRedeemHistoryByPaymentId(String paymentId) {
        Redeemhistory redeem = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Redeemhistory where payment_id = '"+paymentId+"'";
            redeem = em.createQuery (sql, Redeemhistory.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();
            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return redeem;
    }

    @Transactional
    @Override
    public Integer saveRedeemCouponHistory(JSONObject jsonObject) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "RCPN-";
        uniqueId += UUID.randomUUID().toString();
        logger.info ("Entity manager {}",em);

        try {
            String paymentId = (String) jsonObject.get ("paymentId");
            String couponId = (String) jsonObject.get ("couponId");
            String memberId = (String) jsonObject.get ("memberId");

            Redeemhistory redeemhistory = getRedeemHistoryByPaymentId (paymentId);
            if(redeemhistory==null){
                String sql = "INSERT into Redeemhistory (id_redeem,payment_id," +
                        "coupon_id, member_id)" +
                        "values(?,?,?,?)";

                logger.info (sql);
                saveCount = em.createNativeQuery (sql)
                        .setParameter (1,uniqueId)
                        .setParameter (2,paymentId)
                        .setParameter (3,couponId)
                        .setParameter (4,memberId)
                        .executeUpdate ();
            }

            em.getTransaction ().commit ();
            logger.info ("save redeem history success");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
            saveCount = 0;
            logger.info ("save redeem history failed");
        }
        em.close ();
        return saveCount;
    }
}
