package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class CouponNotFoundException extends RuntimeException{
    public CouponNotFoundException() {
        super("Coupon not found");
    }
}
