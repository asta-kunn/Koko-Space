package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class InvalidTokenException extends RuntimeException{
    public InvalidTokenException(){
        super("Verification token invalid");
    }
}
