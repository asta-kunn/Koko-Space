package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class AmountAndRentalNameRequiredException extends RuntimeException {
    public AmountAndRentalNameRequiredException() {
        super("Amount and Rental Name field is required");
    }
}
