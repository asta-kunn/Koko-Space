package id.ac.ui.cs.advprog.coworkingspace.auth.exceptions;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException() {
        super("JWT token is invalid");
    }

}
