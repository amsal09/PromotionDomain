package com.danapprentech.promotion.repositories.implementations;

import com.danapprentech.promotion.models.Coupon;
import com.danapprentech.promotion.repositories.interfaces.ICouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Repository
public class CouponRepository implements ICouponRepository {

    private EntityManagerFactory emf;
    @Autowired
    public CouponRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Coupon> getAllCoupons() {
        EntityManager em = getEntityManager ();
        em.getTransaction ().begin ();
        String sql = "from Coupon";
        return em.createQuery (sql, Coupon.class)
                .getResultList ();
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager ();
    }
}
