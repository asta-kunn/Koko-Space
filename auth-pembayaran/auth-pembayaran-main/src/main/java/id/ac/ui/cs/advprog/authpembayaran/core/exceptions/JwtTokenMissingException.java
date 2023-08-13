package id.ac.ui.cs.advprog.authpembayaran.core.exceptions;

public class JwtTokenMissingException extends RuntimeException {
    public JwtTokenMissingException() {
        super("JWT token is missing");
    }
}
