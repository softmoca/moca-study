package com.example.board.common.exception;

public class UnsupportedTokenException extends JwtAuthenticationException {
    public UnsupportedTokenException(String message) {
        super("UNSUPPORTED_TOKEN", message);
    }

    public UnsupportedTokenException(String message, Throwable cause) {
        super("UNSUPPORTED_TOKEN", message, cause);
    }
}
