package com.example.board.common.exception;

public class ExpiredTokenException extends JwtAuthenticationException {
    public ExpiredTokenException(String message) {
        super("EXPIRED_TOKEN", message);
    }

    public ExpiredTokenException(String message, Throwable cause) {
        super("EXPIRED_TOKEN", message, cause);
    }
}
