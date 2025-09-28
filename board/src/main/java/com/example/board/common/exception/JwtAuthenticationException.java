package com.example.board.common.exception;

public abstract class JwtAuthenticationException extends RuntimeException {
    private final String errorCode;

    protected JwtAuthenticationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected JwtAuthenticationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
