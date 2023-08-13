package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(){
        super("Another user have been made with the same email");
    }
}
