package uk.co.risi.gateway.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GatewayExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(InsufficientVinPermissionException exc) {
        RestErrorResponse error = new RestErrorResponse();
        error.setStatus(HttpStatus.UNAUTHORIZED.value());
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @Data
    private static class RestErrorResponse {
        private int status;
        private String message;
        private LocalDateTime dateTime = LocalDateTime.now();
    }
}
