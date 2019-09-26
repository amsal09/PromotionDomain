package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class CouponRepository implements ICouponRepository {

    private EntityManagerFactory emf;

    @Autowired
    public CouponRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public List<Coupon> getCouponDetailsByName(String couponName) {
        List<Coupon> couponList = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {
            String sql = "from Coupon where coupon_name = '"+couponName+"' " +
                    "and coupon_status = 'not available'";
            couponList = em.createQuery (sql)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return couponList;
    }

    @Override
    public Coupon getCouponDetailsById(String couponID) {
        Coupon coupon = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {
            String sql = "from Coupon where coupon_id = '"+couponID+"'";
            coupon = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return coupon;
    }

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> couponList = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {
            String sql = "from Coupon";
            couponList = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return couponList;
    }

    @Override
    public List<Coupon> getCouponRecommendation(String memberPhone, Long amount) {
        List<Coupon> couponList= null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {

            String sql = "from Coupon " +
                    "where member_phone = '"+memberPhone+"'"+" " +
                    "and coupon_amount between 1000 and "+amount+" " +
                    "and coupon_expired >= :time " +
                    "and coupon_status = 'available' order by coupon_amount desc";

            couponList = em.createQuery (sql)
                    .setParameter ("time", new SimpleDateFormat ("yyyy-MM-dd").format(new Date ()))
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();

        return couponList;
    }

    @Override
    public List<Coupon> getAllCouponByMember(String memberPhone) {
        List<Coupon> couponList= null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {
            String sql = "from Coupon where member_phone ='" +memberPhone+"'"+" " +
                    "order by coupon_amount desc";

            couponList = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return couponList;
    }

    @Override
    public Integer saveOrUpdate(String memberPhone,Long amount) {
        EntityManager em = getEntityManager ();
        long getVoucher = 0L;
        LocalDate ld = LocalDate.now ().plusDays (4);
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "CPN-";
        uniqueId += UUID.randomUUID().toString();
        try {

            String sql = "INSERT into Coupon (coupon_id,member_phone,coupon_name, " +
                    "coupon_amount, coupon_status, coupon_expired)" +
                    "values(?,?,?,?,?,?)";

            if(amount % 25000 == 0){
                getVoucher = (amount/25000) * 3000;

            }else{
                getVoucher = (int)(amount/25000) * 3000;
            }
            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,memberPhone)
                    .setParameter (3,"Coupon Pulsa "+getVoucher)
                    .setParameter (4,getVoucher)
                    .setParameter (5,"available")
                    .setParameter (6,ld.toString ())
                    .executeUpdate ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return saveCount;
    }

    @Override
    public Integer updateStatus(String couponID) {

        int updateCount = 0;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        try {
            String sql = "update Coupon set coupon_status = 'not available' where coupon_id = '"+couponID+"'";

            updateCount = em.createNativeQuery (sql)
                    .executeUpdate ();

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
        }
        em.close ();
        return updateCount;
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }
}
