package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class UsernameEmailPasswordFieldRequiredException extends RuntimeException {
    public UsernameEmailPasswordFieldRequiredException() {super("All fields required to be filled");}
}
