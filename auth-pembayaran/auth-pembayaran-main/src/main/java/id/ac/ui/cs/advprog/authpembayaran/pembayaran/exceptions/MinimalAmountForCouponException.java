package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class MinimalAmountForCouponException extends RuntimeException {
    public MinimalAmountForCouponException() {
        super("Amount is lower than minimum price for coupon");
    }
}
