package id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponUsageRepository extends JpaRepository<CouponUsage, Long> {

    @Query("SELECT COUNT(c) FROM CouponUsage c WHERE c.coupon.id = :couponId")
    Long countByCouponId(@Param("couponId") Long couponId);
}
