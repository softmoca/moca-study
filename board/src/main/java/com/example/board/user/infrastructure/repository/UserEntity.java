package com.example.board.user.infrastructure.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.example.board.user.domain.model.*;
import lombok.*;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {

    @Id
    private String userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserEntityRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;



    // 생성자는 수동으로 작성
    public UserEntity(String userId, String email, String username, String password,
                      UserEntityRole role, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean active) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.active = active;
    }

    // 도메인 모델로 변환
    public User toDomain() {
        return new User(
                UserId.of(this.userId),
                Email.of(this.email),
                this.username,
                Password.createEncoded(this.password),
                mapToDomainRole(this.role),
                this.createdAt,
                this.updatedAt,
                this.active
        );
    }

    // 도메인 모델에서 변환
    public static UserEntity fromDomain(User user) {
        return new UserEntity(
                user.getUserId().getValue(),
                user.getEmail().getValue(),
                user.getUsername(),
                user.getPassword().getValue(),
                mapToEntityRole(user.getRole()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive()
        );
    }

    // 역할 매핑 메서드들
    private static UserRole mapToDomainRole(UserEntityRole entityRole) {
        return switch (entityRole) {
            case ADMIN -> UserRole.ADMIN;
            case USER -> UserRole.USER;
        };
    }

    private static UserEntityRole mapToEntityRole(UserRole domainRole) {
        return switch (domainRole) {
            case ADMIN -> UserEntityRole.ADMIN;
            case USER -> UserEntityRole.USER;
        };
    }

}