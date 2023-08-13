package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class InvalidInputFormatEmailException extends RuntimeException{
    public InvalidInputFormatEmailException(){
        super("Invalid email format");
    }
}
