package com.example.board.common.exception;

public class AuthenticationProcessingException extends JwtAuthenticationException {
    public AuthenticationProcessingException(String message) {
        super("AUTHENTICATION_ERROR", message);
    }

    public AuthenticationProcessingException(String message, Throwable cause) {
        super("AUTHENTICATION_ERROR", message, cause);
    }
}
