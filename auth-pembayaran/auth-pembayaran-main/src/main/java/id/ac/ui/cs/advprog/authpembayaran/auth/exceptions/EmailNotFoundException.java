package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(){
        super("Email not found");
    }
}
