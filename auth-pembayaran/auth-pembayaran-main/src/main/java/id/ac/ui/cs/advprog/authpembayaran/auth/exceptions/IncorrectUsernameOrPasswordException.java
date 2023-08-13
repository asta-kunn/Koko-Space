package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class IncorrectUsernameOrPasswordException extends RuntimeException{
    public IncorrectUsernameOrPasswordException(){
        super("Incorrect Username or Password");
    }
}
