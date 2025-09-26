package com.example.board.user.application.dto;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserRole;
import lombok.*;
import java.time.LocalDateTime;


@Getter
@RequiredArgsConstructor
public class UserResponse {
    private final String userId;
    private final String email;
    private final String username;
    private final UserRole role;
    private final LocalDateTime createdAt;
    private final boolean active;


    public static UserResponse from(User user) {
        return new UserResponse(
                user.getUserId().getValue(),
                user.getEmail().getValue(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.isActive()
        );
    }

}