package id.ac.ui.cs.advprog.authpembayaran.pembayaran.service;

import id.ac.ui.cs.advprog.authpembayaran.pembayaran.dto.*;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.CouponNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.InvalidCouponInputException;
import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.UserNotFoundException;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.model.Coupon;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@RequiredArgsConstructor
@Service
public class CouponLogic {
    @Autowired
    private final CouponServiceImpl couponService;
    
    public Coupon createCouponLogic(CouponCURequest request) {
        couponDataValidation(request);
        
        return couponService.createCoupon(request);
    }

    public void deleteCouponLogic(CouponDeleteRequest request, Long id) {
        if (request.getUserId() == null || request.getUserId() < 0) {
            throw new UserNotFoundException();
        }

        if (id == null) {
            throw new InvalidCouponInputException("Coupon Id required");
        }

        couponService.deleteCoupon(request, id);
    }

    public Coupon updateCouponLogic(CouponCURequest request, Long id) {
        couponDataValidation(request);
        
        if (id == null) {
            throw new InvalidCouponInputException("Id cannot be null");
        }
        
        return couponService.updateCoupon(request, id);
    }
    
    public Double getPriceAfterDiscountLogic(CouponUseRequest request, Long id) {
        couponUseDataValidation(request, id);
        
        return couponService.getPriceAfterDiscount(request, id);
    }
    
    private void couponDataValidation(CouponCURequest request) {
        if (request.getCode() == null ||
                request.getDiscount() == null ||
                request.getEndDate() == null ||
                request.getStartDate() == null ||
                request.getMinPrice() == null ||
                request.getMaxUsage() == null ||
                request.getUserId() == null) {
            throw new InvalidCouponInputException("Input field cannot be empty");
        }
        
        if (request.getDiscount() <= 0) {
            throw new InvalidCouponInputException("Discount cannot be less or equal than zero");
        }

        if (request.getStartDate().after(request.getEndDate())) {
            throw new InvalidCouponInputException("Start date cannot be after end date");
        }

        Calendar cal = Calendar.getInstance();
        if (request.getEndDate().before(cal.getTime())){
            throw new InvalidCouponInputException("End date cannot be before current date");
        }
        
        if (request.getMinPrice() < 0) {
            throw new InvalidCouponInputException("Minimum price cannot be negative");
        }

        if (request.getMaxUsage() < 1) {
            throw new InvalidCouponInputException("Maximum usage cannot be less than zero");
        }
    }
    
    private void couponUseDataValidation(CouponUseRequest request, Long id) {
        if (request.getUserId() == null) {
            throw new UserNotFoundException();
        }
        
        if (request.getPrice() == null) {
            throw new InvalidCouponInputException("Price cannot be null");
        }
        
        if (id == null) {
            throw new CouponNotFoundException();
        }
    }
}
