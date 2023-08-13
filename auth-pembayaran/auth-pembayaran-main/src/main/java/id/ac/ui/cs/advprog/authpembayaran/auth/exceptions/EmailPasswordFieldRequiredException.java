package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class EmailPasswordFieldRequiredException extends RuntimeException{
    public EmailPasswordFieldRequiredException(){
        super("Email and Password field is required");
    }
}
