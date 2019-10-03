package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.IMasterCouponRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.UUID;

@Repository
public class MasterCouponRepository implements IMasterCouponRepository {
    private static final Logger logger = LoggerFactory.getLogger(MasterCouponRepository.class);
    @Autowired
    private EntityManagerFactory emf;

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }

    @Override
    public Integer saveOrUpdate(Mcoupon mcoupon) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "MCPN-";
        uniqueId += UUID.randomUUID().toString();
        logger.info ("Entity manager {}",em);
        try {

            String sql = "INSERT into Mcoupon (m_coupon_id,m_coupon_description, " +
                    "m_coupon_amount, m_minimum_transaction,payment_method)" +
                    "values(?,?,?,?,?)";

            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,mcoupon.getmCouponDescription ())
                    .setParameter (3,mcoupon.getmCouponAmount ())
                    .setParameter (4,mcoupon.getmMinimumTransaction ())
                    .setParameter (5,mcoupon.getPaymentMethod ())
                    .executeUpdate ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return saveCount;
    }

    @Override
    public Mcoupon getDetailById(String mCouponId) {

        Mcoupon mcoupon = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Mcoupon where m_coupon_id = '"+mCouponId+"'";

            mcoupon = em.createQuery (sql, Mcoupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return mcoupon;
    }

    @Override
    public Mcoupon getAllById(String mCouponId, Long amount) {

        Mcoupon mcoupon = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Mcoupon where m_coupon_id = '"+mCouponId+"'"+"" +
                    " and m_coupon_amount between 1000 and "+amount+"" +
                    "order by m_coupon_amount desc";

            mcoupon = em.createQuery (sql, Mcoupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return mcoupon;
    }

    @Override
    public List<Mcoupon> checkMinimumTransaction(Long amount) {
        List<Mcoupon> mcouponList = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {

            String sql = "from Mcoupon where m_minimum_transaction <= "+amount+ " " +
                    "order by m_minimum_transaction desc";

            mcouponList = em.createQuery (sql, Mcoupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return mcouponList;
    }

    @Override
    public Mcoupon getCouponNewMember(String description) {
        Mcoupon mcoupon = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Mcoupon where m_coupon_description = '"+description+"'";

            mcoupon = em.createQuery (sql, Mcoupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return mcoupon;
    }
}
