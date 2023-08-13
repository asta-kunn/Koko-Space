package id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import org.springframework.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @NonNull
    Optional<Coupon> findById(@NonNull Integer id);

    @NonNull
    List<Coupon> findAll();

    @Query("select c from Coupon c where c.endDate < ?1")
    List<Coupon> findAllExpired(Date date);
    
    List<Coupon> findAllByUserId(Integer userId);
    
    @Query("select c from Coupon c where c.user.id = ?1 and c.status = 'Available'")
    List<Coupon> findAllAvailableByUserId(int userId);
    
    @Query("select c from Coupon c where c.status = 'Available'")
    List<Coupon> findAllAvailable();
}
