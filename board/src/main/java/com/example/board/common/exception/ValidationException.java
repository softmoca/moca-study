package com.example.board.common.exception;

public class ValidationException extends BusinessException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(String.format("Validation error for field '%s': %s", field, message));
    }
}