package id.ac.ui.cs.advprog.authpembayaran.core.exceptions.advice;

import id.ac.ui.cs.advprog.authpembayaran.auth.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.pembayaran.exceptions.*;
import id.ac.ui.cs.advprog.authpembayaran.core.exceptions.ErrorTemplate;
import id.ac.ui.cs.advprog.authpembayaran.core.exceptions.JwtInvalidException;
import id.ac.ui.cs.advprog.authpembayaran.core.exceptions.JwtTokenMissingException;

import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.AmountMethodDetailRequiredException;
import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.InvalidTopupAmountException;
import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.InvalidTopupWalletHistoryException;
import id.ac.ui.cs.advprog.authpembayaran.wallet.exceptions.WalletHistoryNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(value = { IncorrectEmailOrPasswordException.class, ResetPasswordTokenExpiredException.class,
                        JwtInvalidException.class, JwtTokenMissingException.class, AccessDeniedException.class,
                        AuthorizationServiceException.class, InvalidTokenException.class, UserNotAllowedException.class,
                        UpdateNotAllowedException.class })
        public ResponseEntity<Object> credentialsError(Exception exception) {
                HttpStatus status = HttpStatus.UNAUTHORIZED;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);

                return new ResponseEntity<>(baseException, status);
        }

        @ExceptionHandler(value = { EmailNotFoundException.class, UserNotFoundException.class,
                        CouponNotFoundException.class,
                        WalletHistoryNotFoundException.class })
        public ResponseEntity<Object> notFoundError(Exception exception) {
                HttpStatus status = HttpStatus.NOT_FOUND;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);

                return new ResponseEntity<>(baseException, status);
        }

        @ExceptionHandler(value = { InvalidEmailInputException.class, EmailPasswordFieldRequiredException.class,
                        InvalidInputFormatPasswordException.class, UsernameEmailPasswordFieldRequiredException.class,
                        InvalidInputFormatEmailException.class, UserAlreadyExistException.class,
                        SecretTokenNewPasswordFieldRequiredException.class, InvalidAmountException.class,
                        AmountAndRentalNameRequiredException.class, MinimalAmountForCouponException.class,
                        AmountMethodDetailRequiredException.class, InvalidTopupAmountException.class,
                        InvalidTopupWalletHistoryException.class, VerificationTokenExpiredException.class,
                        InvalidCouponInputException.class, InvalidUseException.class })
        public ResponseEntity<Object> invalidInputError(Exception exception) {
                HttpStatus status = HttpStatus.BAD_REQUEST;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);
                return new ResponseEntity<>(baseException, status);
        }

        @ExceptionHandler(value = { SendEmailFailedException.class })
        public ResponseEntity<Object> serverError(Exception exception) {
                HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);
                return new ResponseEntity<>(baseException, status);
        }

        @ExceptionHandler(value = { InsufficientFundsException.class })
        public ResponseEntity<Object> paymentError(Exception exception) {
                HttpStatus status = HttpStatus.PAYMENT_REQUIRED;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);
                return new ResponseEntity<>(baseException, status);
        }

        @ExceptionHandler(value = { UserAlreadyVerifiedException.class })
        public ResponseEntity<Object> alreadyVerifiedError(Exception exception) {
                HttpStatus status = HttpStatus.CONFLICT;
                ErrorTemplate baseException = new ErrorTemplate(
                                exception.getMessage(),
                                status);
                return new ResponseEntity<>(baseException, status);
        }

}
