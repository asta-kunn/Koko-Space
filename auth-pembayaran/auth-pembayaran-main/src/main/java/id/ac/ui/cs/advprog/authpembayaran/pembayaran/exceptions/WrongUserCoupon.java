package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class WrongUserCoupon extends RuntimeException {
    public WrongUserCoupon() {
        super("The Coupon doesn't belong to you");
    }
}
