package id.ac.ui.cs.advprog.coworkingspace.auth.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorTemplate(String message, HttpStatus httpStatus) {
}
