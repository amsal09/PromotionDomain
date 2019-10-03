package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.models.Mcoupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import com.danapprentech.promotion.response.CouponIssue;
import com.danapprentech.promotion.services.interfaces.ICouponHistoryService;
import com.danapprentech.promotion.services.interfaces.IMasterCouponService;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Transactional
@Repository
public class CouponRepository implements ICouponRepository {
    private static final Logger logger = LoggerFactory.getLogger(CouponRepository.class);

    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private IMasterCouponService iMasterCouponService;
    @Autowired
    private ICouponHistoryService iCouponHistoryService;


    @Override
    public CouponIssue getCouponDetailsById(String couponID) {
        Coupon coupon = null;
        CouponIssue couponIssue =null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Coupon where coupon_id = '"+couponID+"'";
            logger.info ("start query {} ",sql);
            coupon = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getSingleResult ();

            Mcoupon mcoupon = iMasterCouponService.getDetailById (coupon.getmCouponId ());

            couponIssue = new CouponIssue.CouponIssuebuilder ()
                    .withCouponId (coupon.getCouponId ())
                    .withMemberId (coupon.getMemberId ())
                    .withCouponName (mcoupon.getmCouponDescription ())
                    .withCouponAmount (mcoupon.getmCouponAmount ())
                    .withPaymentMethod (mcoupon.getPaymentMethod ())
                    .withCouponStatus (coupon.getCouponStatus ())
                    .withCouponExpired (coupon.getCouponExpired ())
                    .build ();

            logger.info ("{}",em);
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return couponIssue;
    }

    @Override
    public List<Coupon> getAllCoupons() {
        List<Coupon> couponList = null;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String sql = "from Coupon";
            couponList = em.createQuery (sql, Coupon.class)
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return couponList;
    }

    @Override
    public List<CouponIssue> getCouponRecommendation(JSONObject jsonObject) {
        List<Coupon> couponList= null;
        ArrayList<Mcoupon> mcouponList = new ArrayList<Mcoupon> ();
        ArrayList<CouponIssue> couponIssueList = new ArrayList<CouponIssue> ();
        EntityManager em = getEntityManager ();

        String memberId = (String) jsonObject.get ("memberId");
        Number number = (Number) jsonObject.get ("amount");
        Long value = number.longValue ();

        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {

            String sql = "from Coupon where member_id = '"+memberId+"'"+" " +
                    "and coupon_expired >= :time " +
                    "and coupon_status = 'available'";

            System.out.println (sql);
            couponList = em.createQuery (sql)
                    .setParameter ("time", new SimpleDateFormat ("yyyy-MM-dd").format(new Date ()))
                    .setLockMode (LockModeType.PESSIMISTIC_WRITE)
                    .getResultList ();

            em.getTransaction ().commit ();

            for (Coupon coupon: couponList) {
                mcouponList.add (iMasterCouponService.getAllById (coupon.getmCouponId (), value));
            }

            for(int i=0; i<=couponList.size (); i++){
                CouponIssue couponIssue = new CouponIssue.CouponIssuebuilder ()
                        .withCouponId (couponList.get (i).getCouponId ())
                        .withMemberId (couponList.get (i).getMemberId ())
                        .withCouponAmount (mcouponList.get (i).getmCouponAmount ())
                        .withCouponName (mcouponList.get (i).getmCouponDescription ())
                        .withPaymentMethod (mcouponList.get (i).getPaymentMethod ())
                        .withCouponExpired (couponList.get (i).getCouponExpired ())
                        .withCouponStatus (couponList.get (i).getCouponStatus ())
                        .build ();

                couponIssueList.add (couponIssue);
            }


        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();

        return couponIssueList;
    }


    @Override
    public Integer saveOrUpdate(JSONObject jsonObject) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "TCPN-";
        logger.info ("Entity manager {}",em);
        try {
            LocalDate ld = LocalDate.now ().plusDays (20);
            uniqueId += UUID.randomUUID().toString();
            Number number = (Number) jsonObject.get ("amount");
            Long value = number.longValue ();
            String memberID = (String) jsonObject.get ("memberId");

            List<Mcoupon> mcoupons = iMasterCouponService.checkMinimumTransaction (value);

            String sql = "INSERT into Coupon (coupon_id,member_id,m_coupon_id, " +
                    "coupon_status, coupon_expired)" +
                    "values(?,?,?,?,?)";

            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,memberID)
                    .setParameter (3,mcoupons.get (0).getmCouponId ())
                    .setParameter (4,"available")
                    .setParameter (5,ld.toString ())
                    .executeUpdate ();

            if(saveCount!=0){
                String getResponse = iCouponHistoryService.addHistory (jsonObject);
                while (getResponse.equalsIgnoreCase ("failed")){
                    getResponse = iCouponHistoryService.addHistory (jsonObject);
                }
            }
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return saveCount;
    }

    @Override
    public CouponIssue updateStatus(JSONObject jsonObject) {

        int updateCount=0;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        String couponID = (String) jsonObject.get ("couponId");
        String paymentCode = (String) jsonObject.get ("paymentMethodCode");
        try {

            CouponIssue couponIssue = getCouponDetailsById (couponID);

            if(paymentCode.equalsIgnoreCase (couponIssue.getPaymentMethod ())){

                String sql = "update Coupon set coupon_status = 'not available' where coupon_id = '"+couponID+"'"
                        +"and coupon_status = 'available'";

                updateCount = em.createNativeQuery (sql)
                        .executeUpdate ();
            }

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return getCouponDetailsById (couponID);
    }

    @Override
    public Integer updateStatusTrue(JSONObject jsonObject) {
        int updateCount = 0;
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        logger.info ("Entity manager {}",em);
        try {
            String couponID = (String) jsonObject.get ("couponId");
            String sql = "update Coupon set coupon_status = 'available' where coupon_id = '"+couponID+"'"
                    +" and coupon_status ='not available'";

            updateCount = em.createNativeQuery (sql)
                    .executeUpdate ();

            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return updateCount;
    }

    @Override
    public Integer firstCoupon(JSONObject jsonObject) {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        int saveCount = 0;
        String uniqueId = "TCPN-";
        logger.info ("Entity manager {}",em);
        try {
            LocalDate ld = LocalDate.now ().plusDays (10);
            uniqueId += UUID.randomUUID().toString();
            String memberID = (String) jsonObject.get ("memberId");

            Mcoupon mcoupon = iMasterCouponService.getCouponNewMember ((String) jsonObject.get ("status"));

            String sql = "INSERT into Coupon (coupon_id,member_id,m_coupon_id, " +
                    "coupon_status, coupon_expired)" +
                    "values(?,?,?,?,?)";

            saveCount = em.createNativeQuery (sql)
                    .setParameter (1,uniqueId)
                    .setParameter (2,memberID)
                    .setParameter (3,mcoupon.getmCouponId ())
                    .setParameter (4,"available")
                    .setParameter (5,ld.toString ())
                    .executeUpdate ();
            em.getTransaction ().commit ();
        }catch (Exception e){
            em.getTransaction ().rollback ();
            logger.warn ("Error: {} - {}",e.getMessage (),e.getStackTrace ());
        }
        em.close ();
        return saveCount;
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }
}
