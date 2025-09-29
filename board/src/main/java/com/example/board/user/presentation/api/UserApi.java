package com.example.board.user.presentation.api;

import com.example.board.user.application.dto.UserCreateRequest;
import com.example.board.user.application.dto.UserResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@Tag(name = "👤 사용자", description = "사용자 관리 관련 API")
public interface UserApi {

    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = UserResponse.class),
                            examples = @ExampleObject(
                                    value = """
                        {
                          "userId": 1,
                          "email": "newuser@example.com",
                          "username": "newuser",
                          "role": "USER",
                          "createdAt": "2024-01-01T10:00:00",
                          "active": true
                        }
                        """
                            )
                    )
            )
    })
    ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request);

    @Operation(summary = "사용자 정보 조회")
    ResponseEntity<UserResponse> getUser(
            @Parameter(description = "조회할 사용자 ID", required = true, example = "1")
            @PathVariable Long userId
    );
}