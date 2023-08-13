package id.ac.ui.cs.advprog.authpembayaran.pembayaran.controller;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponCURequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponDeleteRequest;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.CouponUseRequest;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.repository.CouponRepository;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.service.CouponLogic;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {
    private final CouponLogic couponLogic;
    private final CouponRepository couponRepository;

    @GetMapping("/get/all")
    public ResponseEntity<List<Coupon>> getAllCoupon() {
        return ResponseEntity.ok(couponRepository.findAllAvailable());
    }

    @GetMapping("/get/id/{id}")
    public ResponseEntity<Coupon> getCouponById(
            @PathVariable("id") Long id) {
        return ResponseEntity.ok(couponRepository.findById(id).orElse(null));
    }

    @GetMapping("/get/user/{userId}")
    public ResponseEntity<List<Coupon>> getCouponByUserId(
            @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(couponRepository.findAllAvailableByUserId(userId));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<Coupon> createCoupon(
            @RequestBody CouponCURequest request) {
        return ResponseEntity.ok(couponLogic.createCouponLogic(request));
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<String> deleteCoupon(
            @PathVariable("id") Long id, @RequestBody CouponDeleteRequest request) {
        couponLogic.deleteCouponLogic(request, id);
        return ResponseEntity.ok(String.format("Deleted Coupon with id %d", id));
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<Coupon> updateCoupon(
            @PathVariable("id") Long id, @RequestBody CouponCURequest request) {
        return ResponseEntity.ok(couponLogic.updateCouponLogic(request, id));
    }

    @GetMapping("/get/price/{id}")
    @PreAuthorize("hasAuthority('PENGGUNA')")
    public ResponseEntity<Double> getPriceAfterDiscount(
            @PathVariable("id") Long id, @RequestBody CouponUseRequest request) {
        return ResponseEntity.ok(couponLogic.getPriceAfterDiscountLogic(request, id));
    }
    
    @GetMapping("/get/allCoupon")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<List<Coupon>> getAllCouponAll() {
        return ResponseEntity.ok(couponRepository.findAll());
    }
    
    @GetMapping("/get/allCoupon/{id}")
    @PreAuthorize("hasAuthority('PENGELOLA')")
    public ResponseEntity<List<Coupon>> getAllCouponById(
            @PathVariable("id") Integer id) {
        return ResponseEntity.ok(couponRepository.findAllByUserId(id));
    }

}
