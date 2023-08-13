package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class UserAlreadyVerifiedException extends RuntimeException{
    public UserAlreadyVerifiedException(){
        super("User account already verified");
    }
}