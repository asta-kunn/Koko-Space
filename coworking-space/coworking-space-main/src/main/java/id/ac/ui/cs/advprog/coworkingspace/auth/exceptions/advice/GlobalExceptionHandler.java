package id.ac.ui.cs.advprog.coworkingspace.auth.exceptions.advice;

import id.ac.ui.cs.advprog.coworkingspace.auth.exceptions.ErrorTemplate;
import id.ac.ui.cs.advprog.coworkingspace.auth.exceptions.JwtInvalidException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.InvalidExtendRentException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.InvalidUpgradeException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.WorkspaceDoesNotExistException;
import id.ac.ui.cs.advprog.coworkingspace.workspace.exceptions.SpaceRentDoesNotExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {JwtInvalidException.class})
    public ResponseEntity<Object> credentialsError(Exception exception) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorTemplate baseException = new ErrorTemplate(exception.getMessage(), status);

        return new ResponseEntity<>(baseException, status);
    }


    @ExceptionHandler(value = {SpaceRentDoesNotExistException.class, WorkspaceDoesNotExistException.class})
    public ResponseEntity<Object> rentAndWorkspaceNotAvailable(Exception exception) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {InvalidExtendRentException.class})
    public ResponseEntity<Object> extendNotHourlySpaceRent(Exception exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {InvalidUpgradeException.class})
    public ResponseEntity<Object> upgradeNotCoworkingSpace(Exception exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest
        );

        return new ResponseEntity<>(baseException, badRequest);
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> generalError(Exception exception) {
        HttpStatus badRequest = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorTemplate baseException = new ErrorTemplate(
                exception.getMessage(),
                badRequest
        );

        return new ResponseEntity<>(baseException, badRequest);
    }


}

