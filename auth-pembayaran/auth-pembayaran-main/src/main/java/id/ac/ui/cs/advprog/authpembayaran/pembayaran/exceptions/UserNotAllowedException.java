package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class UserNotAllowedException extends RuntimeException{
    public UserNotAllowedException() {
        super("Changes not allowed, wrong user");
    }
}
