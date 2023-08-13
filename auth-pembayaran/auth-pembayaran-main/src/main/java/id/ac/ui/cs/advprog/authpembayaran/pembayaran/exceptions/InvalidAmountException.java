package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class InvalidAmountException extends RuntimeException{
    public InvalidAmountException(){
        super("Invalid amount. The amount cannot be negative");
    }
}
