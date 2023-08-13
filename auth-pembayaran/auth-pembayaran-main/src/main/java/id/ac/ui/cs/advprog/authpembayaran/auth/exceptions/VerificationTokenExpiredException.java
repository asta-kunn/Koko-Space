package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class VerificationTokenExpiredException extends RuntimeException{
    public VerificationTokenExpiredException(){
        super("Verification token expired");
    }
}
