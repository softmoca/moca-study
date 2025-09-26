package com.example.board.user.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

public class LoginRequest {

    @NotBlank(message = "이메일은 필수입니다")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    private String password;

    // 기본 생성자
    public LoginRequest() {}

    // 생성자
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter/Setter
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
