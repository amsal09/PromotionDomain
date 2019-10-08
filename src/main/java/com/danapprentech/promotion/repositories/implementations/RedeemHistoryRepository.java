package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Redeemhistory;
import com.danapprentech.promotion.repositories.interfaces.IRedeemHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;

@Repository
@EnableTransactionManagement
public class RedeemHistoryRepository implements IRedeemHistoryRepository {
    private static final Logger logger = LoggerFactory.getLogger(RedeemHistoryRepository.class);
    @Autowired
    private EntityManagerFactory emf;

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }


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
}
