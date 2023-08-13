package id.ac.ui.cs.advprog.authpembayaran.core.exceptions;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException() {
        super("JWT token is invalid");
    }

}
