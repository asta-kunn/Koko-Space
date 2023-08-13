package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Insufficient funds. Your account balance is not enough to make this payment.");
    }
}
