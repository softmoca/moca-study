package com.example.board.common.util;

import org.springframework.stereotype.Component;

@Component("customPasswordEncoder") // Bean 이름 지정으로 충돌 방지
public class PasswordEncoder {

    private final org.springframework.security.crypto.password.PasswordEncoder encoder;

    public PasswordEncoder(org.springframework.security.crypto.password.PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}