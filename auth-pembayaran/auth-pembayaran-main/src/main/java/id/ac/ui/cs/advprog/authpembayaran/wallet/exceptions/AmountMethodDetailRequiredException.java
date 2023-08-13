package id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions;

public class AmountMethodDetailRequiredException extends RuntimeException{
    public AmountMethodDetailRequiredException() {
            super("Amount, Method, and Detail field is required");
        }
}
