package id.ac.ui.cs.advprog.authpembayaran.pembayaran.component;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.CouponNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.CouponUsage;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Status;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponUsageRepository;

import java.util.Calendar;

public class CouponUseManager {
    @SuppressWarnings("squid:S1068")
    private CouponUsageRepository couponUsageRepository;
    @SuppressWarnings("squid:S1068")
    private CouponRepository couponRepository;
    @SuppressWarnings("squid:S1068")
    private UserRepository userRepository;

    private static CouponUseManager instance;

    @SuppressWarnings("squid:S1186")
    public CouponUseManager() {
    }

    public static CouponUseManager getInstance() {
        if (instance == null) {
            instance = new CouponUseManager();
        }

        return instance;
    }
    
    public synchronized Double useCoupon(Integer id, Integer userId, UserRepository userRepository, CouponRepository couponRepository, CouponUsageRepository couponUsageRepository){
        
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        
        Coupon coupon = couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);

        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (userLimitCheck(user, coupon) && dateLimitCheck(coupon)) {
            if (couponUsageRepository.countByCouponId(coupon.getId()) < coupon.getMaxUsage()) {
                if (couponUsageRepository.countByCouponId(coupon.getId()) == coupon.getMaxUsage() - 1) {
                    coupon.setStatus(Status.MAX_USED);
                }

                CouponUsage couponUsage = new CouponUsage(coupon, user);
                user.add(couponUsage);
                coupon.add(couponUsage);
                userRepository.save(user);
                couponRepository.save(coupon);
                couponUsageRepository.save(couponUsage);
                return coupon.getDiscount();
            }
            return 0.0;
        }
        return 0.0;

    }

    private boolean userLimitCheck(User user, Coupon coupon) {
        for (CouponUsage couponUse : user.getCouponUsed()) {
            if (couponUse.getCoupon().equals(coupon)) {
                return false;
            }
        }
        return true;
    }

    private boolean dateLimitCheck( Coupon coupon) {
        Calendar cal = Calendar.getInstance();
        if (cal.getTime().after(coupon.getStartDate()) && cal.getTime().before(coupon.getEndDate())) {
            return true;
        }
        coupon.setStatus(Status.EXPIRED);
        return false;
    }
    
    public Double getPrice(CouponUseRequest request, Long id, UserRepository userRepository, CouponRepository couponRepository, CouponUsageRepository couponUsageRepository){
        
        this.userRepository = userRepository;
        this.couponRepository = couponRepository;
        this.couponUsageRepository = couponUsageRepository;
        
        Coupon coupon = couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);

        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);

        if (userLimitCheck(user, coupon) && dateLimitCheck(coupon) && couponUsageRepository.countByCouponId(coupon.getId()) < coupon.getMaxUsage()) {
            return request.getPrice() - coupon.getDiscount();
        }
        return request.getPrice();
    }
}
