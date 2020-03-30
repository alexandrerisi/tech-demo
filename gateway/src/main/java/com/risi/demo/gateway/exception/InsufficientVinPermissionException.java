package com.risi.demo.gateway.exception;

public class InsufficientVinPermissionException extends RuntimeException {
    public InsufficientVinPermissionException(String message) {
        super(message);
    }
}
