package com.example.board.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "로그인 요청")
public class LoginRequest {

    @NotBlank(message = "이메일은 필수입니다")
    @Schema(
            description = "로그인할 이메일 주소",
            example = "user@example.com",
            required = true,
            format = "email"
    )
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Schema(
            description = "비밀번호",
            example = "password123!",
            required = true,
            format = "password"
    )
    private String password;
}
