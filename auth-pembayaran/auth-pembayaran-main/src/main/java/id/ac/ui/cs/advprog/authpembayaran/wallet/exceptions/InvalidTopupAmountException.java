package id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions;

public class InvalidTopupAmountException extends RuntimeException{
    public InvalidTopupAmountException(){
        super("Invalid amount. The amount cannot be negative");
    }
}
