package id.ac.ui.cs.advprog.authpembayaran.auth.exceptions;

public class InvalidInputFormatPasswordException extends RuntimeException{
  public InvalidInputFormatPasswordException() {
      super("The password must be at least 8 characters long.\n" +
              "    The password must contain at least one uppercase letter.\n" +
              "    The password must contain at least one lowercase letter.\n" +
              "    The password must contain at least one digit.\n" +
              "    The password must contain at least one special character (such as !, @, #, $, %, ^, &, *, (, ), -, _, +, =, {, }, [, ], |, , :, ;, \", ', <, >, ,, ., ?, /).");
  }
}
