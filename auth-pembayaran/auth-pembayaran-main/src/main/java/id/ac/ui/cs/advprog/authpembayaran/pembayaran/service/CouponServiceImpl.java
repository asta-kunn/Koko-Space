package id.ac.ui.cs.advprog.authpembayaran.pembayaran.service;

import id.ac.ui.cs.advprog.authpembayaran.auth.model.User;
import id.ac.ui.cs.advprog.authpembayaran.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.component.CouponUseManager;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponCURequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponDeleteRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.CouponNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.UserNotAllowedException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Status;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponUsageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class CouponServiceImpl implements CouponService {
    @Autowired
    private final CouponRepository couponRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CouponUsageRepository couponUsageRepository;
    
    private final CouponUseManager instance = CouponUseManager.getInstance();
    
    @Override
    public Coupon createCoupon(CouponCURequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);

        Coupon coupon = Coupon.builder()
                .code(request.getCode())
                .discount(request.getDiscount())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .minPrice(request.getMinPrice())
                .maxUsage(request.getMaxUsage())
                .user(user)
                .status(Status.AVAILABLE)
                .build();

        couponRepository.save(coupon);
        
        user.create(coupon);
        
        userRepository.save(user);

        return coupon;
    }

    @Override
    public void deleteCoupon(CouponDeleteRequest request, Long id) {
        Coupon coupon = couponRepository.findById(id).orElse(null);

        if (coupon == null) {
            return;
        }

        if (!Objects.equals(coupon.getUser().getId(), request.getUserId())) {
            throw new UserNotAllowedException();
        }

        couponRepository.delete(coupon);
    }

    @Override
    public Coupon updateCoupon(CouponCURequest request, Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);

        User user = userRepository.findById(request.getUserId()).orElseThrow(UserNotFoundException::new);
        
        if (user != null) {
            coupon.setUser(user);
        }
        
        if (!Objects.equals(request.getUserId(), coupon.getUser().getId())) {
            throw new UserNotAllowedException();
        }

        coupon.setCode(request.getCode());
        coupon.setDiscount(request.getDiscount());
        coupon.setStartDate(request.getStartDate());
        coupon.setEndDate(request.getEndDate());
        coupon.setMinPrice(request.getMinPrice());
        coupon.setMaxUsage(request.getMaxUsage());
        
        if (couponUsageRepository.countByCouponId(coupon.getId()) >= coupon.getMaxUsage()) {
            coupon.setStatus(Status.MAX_USED);
        }

        couponRepository.save(coupon);

        return coupon;
    }

    @Override
    public Double useCoupon(Integer id, Integer userId) {
        return instance.useCoupon(id, userId, userRepository, couponRepository, couponUsageRepository);
    }

    @Override
    public Double getPriceAfterDiscount(CouponUseRequest request, Long id) {
        return instance.getPrice(request, id,  userRepository, couponRepository, couponUsageRepository);
    }
}
