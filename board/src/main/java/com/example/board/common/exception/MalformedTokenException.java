package com.example.board.common.exception;

public class MalformedTokenException extends JwtAuthenticationException {
    public MalformedTokenException(String message) {
        super("MALFORMED_TOKEN", message);
    }

    public MalformedTokenException(String message, Throwable cause) {
        super("MALFORMED_TOKEN", message, cause);
    }
}
