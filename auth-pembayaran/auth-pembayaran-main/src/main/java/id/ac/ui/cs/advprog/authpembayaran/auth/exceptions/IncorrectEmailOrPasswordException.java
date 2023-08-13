package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class IncorrectEmailOrPasswordException extends RuntimeException{
    public IncorrectEmailOrPasswordException(){
        super("Incorrect Email or Password");
    }
}
