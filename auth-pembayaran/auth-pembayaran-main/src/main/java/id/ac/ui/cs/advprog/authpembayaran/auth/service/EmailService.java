package id.ac.ui.cs.advprog.authpembayaran.auth.service;

public interface EmailService {

    public void sendMail(String receiver, String subject, String text);

    public boolean validate(String email);

}
