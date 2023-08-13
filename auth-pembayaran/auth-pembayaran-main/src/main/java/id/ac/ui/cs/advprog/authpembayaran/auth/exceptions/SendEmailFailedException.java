package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class SendEmailFailedException extends RuntimeException{
    public SendEmailFailedException(){
        super("Error while Sending Mail");
    }


}
