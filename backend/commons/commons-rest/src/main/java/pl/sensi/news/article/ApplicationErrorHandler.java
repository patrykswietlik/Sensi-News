package pl.sensi.news.article;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ApplicationErrorHandler {

    private static final String VALIDATION_EXCEPTION = "VALIDATION_EXCEPTION";
    private static final String BAD_CREDENTIALS_EXCEPTIION = "BAD_CREDENTIALS_EXCEPTIION";

    @ExceptionHandler
    public ResponseEntity<ApplicationErrorResponse> handleException(final ApplicationException e, final WebRequest request) {
        return new ResponseEntity<>(new ApplicationErrorResponse(e.getMessage(), LocalDateTime.now(), e.getErrorType()), HttpStatusCode.valueOf(e.getErrorCode()));
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationErrorResponse> handleException(final ConstraintViolationException e, final WebRequest request) {
        return new ResponseEntity<>(new ApplicationErrorResponse(e.getMessage(), LocalDateTime.now(), VALIDATION_EXCEPTION), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApplicationErrorResponse> handleException(final BadCredentialsException e, final WebRequest request) {
        return new ResponseEntity<>(new ApplicationErrorResponse(e.getMessage(), LocalDateTime.now(), BAD_CREDENTIALS_EXCEPTIION), HttpStatus.FORBIDDEN);
    }
}
