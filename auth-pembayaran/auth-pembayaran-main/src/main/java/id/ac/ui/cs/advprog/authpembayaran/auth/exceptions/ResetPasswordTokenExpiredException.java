package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class ResetPasswordTokenExpiredException extends RuntimeException{
    public ResetPasswordTokenExpiredException(){
        super("Reset password token is already expired");
    }
}
