package com.example.board.user.presentation.api;

import com.example.board.user.application.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name = "🔧 관리자", description = "관리자 전용 API")
@PreAuthorize("hasRole('ADMIN')")
public interface AdminApi {

    @Operation(
            summary = "사용자 정보 조회 (관리자용)",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "사용자 조회 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "userId": 1,
                          "email": "user@example.com",
                          "username": "johndoe",
                          "role": "USER",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<UserResponse> getUser(
            @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
            @PathVariable Long userId
    );
}