package id.ac.ui.cs.advprog.authpembayaran.pembayaran.service;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponCURequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponDeleteRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;

public interface CouponService {
    public Coupon createCoupon(CouponCURequest request);

    public void deleteCoupon(CouponDeleteRequest request, Long id);

    public Coupon updateCoupon(CouponCURequest request, Long id);

    public Double useCoupon(Integer id, Integer userId);

    public Double getPriceAfterDiscount(CouponUseRequest request, Long id);
}
