
// UserCreateRequest.java - Swagger 스키마 추가
package com.example.board.user.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "사용자 생성 요청")
public class UserCreateRequest {

    @NotBlank(message = "이메일은 필수입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    @Schema(
            description = "사용자 이메일 주소",
            example = "user@example.com",
            required = true,
            format = "email"
    )
    private String email;

    @NotBlank(message = "사용자명은 필수입니다")
    @Size(min = 3, max = 20, message = "사용자명은 3자 이상 20자 이하여야 합니다")
    @Schema(
            description = "사용자명 (고유해야 함)",
            example = "johndoe",
            required = true,
            minLength = 3,
            maxLength = 20,
            pattern = "^[a-zA-Z0-9_]+$"
    )
    private String username;

    @NotBlank(message = "비밀번호는 필수입니다")
    @Size(min = 8, max = 50, message = "비밀번호는 8자 이상 50자 이하여야 합니다")
    @Schema(
            description = "비밀번호",
            example = "password123!",
            required = true,
            minLength = 8,
            maxLength = 50,
            format = "password"
    )
    private String password;
}