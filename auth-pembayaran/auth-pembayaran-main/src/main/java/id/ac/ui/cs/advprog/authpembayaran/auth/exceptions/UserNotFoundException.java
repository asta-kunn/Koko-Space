package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException() {
        super("User not found");
    }
}
