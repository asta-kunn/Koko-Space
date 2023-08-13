package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class SecretTokenNewPasswordFieldRequiredException extends RuntimeException{
    public SecretTokenNewPasswordFieldRequiredException(){
        super("Secret Token and New Password field required");
    }
}
