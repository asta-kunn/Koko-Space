package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class InvalidUseException extends RuntimeException {
    public InvalidUseException() {
        super("Coupon cannot be used");
    }
}
