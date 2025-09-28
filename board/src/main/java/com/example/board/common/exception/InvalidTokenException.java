package com.example.board.common.exception;

public class InvalidTokenException extends JwtAuthenticationException {
    public InvalidTokenException(String message) {
        super("INVALID_TOKEN", message);
    }

    public InvalidTokenException(String message, Throwable cause) {
        super("INVALID_TOKEN", message, cause);
    }
}
