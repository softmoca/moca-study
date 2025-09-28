package com.example.board.common.exception;

public class InvalidTokenSignatureException extends JwtAuthenticationException {
    public InvalidTokenSignatureException(String message) {
        super("INVALID_SIGNATURE", message);
    }

    public InvalidTokenSignatureException(String message, Throwable cause) {
        super("INVALID_SIGNATURE", message, cause);
    }
}
