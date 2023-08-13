package id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions;

public class UpdateNotAllowedException extends RuntimeException{
    public UpdateNotAllowedException() {
        super("Update not allowed");
    }
}
