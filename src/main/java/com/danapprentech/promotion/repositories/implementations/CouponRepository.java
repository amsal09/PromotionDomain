package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@EnableTransactionManagement
@Repository
public class CouponRepository implements ICouponRepository {
    private static final Logger logger = LoggerFactory.getLogger(CouponRepository.class);

    @Autowired
    private EntityManagerFactory emf;


    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public Coupon getCouponDetailsById(String couponID) {
        Coupon coupon = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Coupon where coupon_id = '"+couponID+"'";
            logger.info ("start query {} ",sql);
            coupon = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();

            logger.info ("{}",em);
            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            logger.info ("Rollback transaction");
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return coupon;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public List<Coupon> getAllCoupons() {
        List<Coupon> couponList = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "FROM Coupon";
            couponList = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();
            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.info ("Rollback transaction");
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return couponList;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public List<Coupon> getCouponRecommendation(JSONObject jsonObject) {
        List<Coupon> couponList= null;

        EntityManager em = getEntityManager ();

        String memberId = (String) jsonObject.get ("memberId");

        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {

            String sql = "from Coupon where member_id = '"+memberId+"'"+" " +
                    "and coupon_expired >= :time " +
                    "and coupon_status = 'available'";

            couponList = em.createQuery (sql)
                    .setParameter ("time", new SimpleDateFormat ("yyyy-MM-dd").format(new Date ()))
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            logger.info ("Rollback transaction");
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return couponList;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public JSONObject saveOrUpdate(JSONObject jsonObject) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "TCPN-";
        logger.info ("Entity manager {}",em);
        uniqueId += UUID.randomUUID().toString();
        JSONObject json = new JSONObject ();
        try {
            LocalDate ld = LocalDate.now ().plusDays (20);
            String memberID = (String) jsonObject.get ("memberId");
            String masterID = (String) jsonObject.get ("masterId");

            String sql = "INSERT into Coupon (coupon_id,member_id,m_coupon_id, " +
                    "coupon_status, coupon_expired)" +
                    "values(?,?,?,?,?)";

            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,memberID)
                    .setParameter (3,masterID)
                    .setParameter (4,"available")
                    .setParameter (5,ld.toString ())
                    .executeUpdate ();

            em.getTransaction ().commit ();
            json.put ("value",saveCount);
            json.put ("key",uniqueId);
            logger.info ("Commit transaction");
        }catch (Exception e){
            logger.info ("Rollback transaction");
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
            json.put ("value",saveCount);
            json.put ("key",uniqueId);
        }
        em.close ();
        return json;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public Integer updateStatus(JSONObject jsonObject) {
        int updateCount=0;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String couponID = (String) jsonObject.get ("couponId");
            String memberId = (String) jsonObject.get ("memberId");
            String sql = "update Coupon set coupon_status = 'not available'"+",update_time = :time where coupon_id = '"+couponID+"'"
                    +"and coupon_status = 'available' and member_id = '"+memberId+"'";

            updateCount = em.createNativeQuery (sql)
                    .setParameter ("time", LocalDateTime.now ())
                    .executeUpdate ();

            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.info ("Rollback transaction");
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return updateCount;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public Integer updateStatusTrue(JSONObject jsonObject) {
        int updateCount = 0;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String couponID = (String) jsonObject.get ("couponId");
            String sql = "update Coupon set coupon_status = 'available', update_time = :time where coupon_id = '"+couponID+"'"
                    +" and coupon_status ='not available'";

            updateCount = em.createNativeQuery (sql)
                    .setParameter ("time", LocalDateTime.now ())
                    .executeUpdate ();

            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.info ("Rollback transaction");
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return updateCount;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public JSONObject firstCoupon(JSONObject jsonObject) {
        EntityManager em = getEntityManager ();
        JSONObject json = new JSONObject ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "TCPN-";
        logger.info ("Entity manager {}",em);
        try {

            LocalDate ld = LocalDate.now ().plusDays (10);
            uniqueId += UUID.randomUUID().toString();
            String memberID = (String) jsonObject.get ("memberId");
            String masterId = (String) jsonObject.get ("masterId");

            String sql = "INSERT into Coupon (coupon_id,member_id,m_coupon_id, " +
                    "coupon_status, coupon_expired)" +
                    "values(?,?,?,?,?)";

            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,memberID)
                    .setParameter (3,masterId)
                    .setParameter (4,"available")
                    .setParameter (5,ld.toString ())
                    .executeUpdate ();

            em.getTransaction ().commit ();
            json.put ("value",saveCount);
            json.put ("key",uniqueId);
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.info ("Rollback transaction");
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
            json.put ("value",saveCount);
            json.put ("key",uniqueId);
        }
        em.close ();
        return json;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public List<Coupon> checkForNewMember(String memberId, String mCouponId) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        List<Coupon> coupon = null;
        try {
            System.out.println (mCouponId);
            String sql = "from Coupon where member_id = '"+memberId+"'"+" " +
                    "and m_coupon_id='"+mCouponId+"'";
            logger.info ("start query {} ",sql);

            coupon = em.createQuery (sql)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            logger.info ("{}",em);
            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            logger.info ("Rollback transaction");
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
            return coupon;
        }
        em.close ();
        return coupon;
    }

    @Retryable(value = { SQLException.class }, maxAttempts = 3)
    @Override
    @Transactional
    public Integer deleteById(String couponId) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "TCPN-";
        logger.info ("Entity manager {}",em);
        try {
            String sql = "DELETE FROM Coupon WHERE coupon_id='"+couponId+"'";

            saveCount = em.createNativeQuery (sql)
                    .executeUpdate ();

            em.getTransaction ().commit ();
            logger.info ("Commit transaction");
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.info ("Rollback transaction");
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
            return saveCount;
        }
        em.close ();
        return saveCount;
    }

    @Recover
    public String recover(SQLException t){
        logger.info("Service recovering");
        return "Service recovered from billing service failure.";
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }
}
