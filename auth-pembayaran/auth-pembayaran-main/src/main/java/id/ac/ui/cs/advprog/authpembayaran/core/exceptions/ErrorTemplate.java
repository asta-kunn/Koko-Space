package id.ac.ui.cs.advprog.authpembayaran.core.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorTemplate(String message, HttpStatus httpStatus) {

}
