package com.example.board.user.infrastructure.repository;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
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
    private UserRole role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Boolean active;

    // 기본 생성자
    protected UserEntity() {}

    // 생성자
    public UserEntity(String userId, String email, String username, String password,
                      UserRole role, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean active) {
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
    public com.example.board.user.domain.model.User toDomain() {
        return new com.example.board.user.domain.model.User(
                com.example.board.user.domain.model.UserId.of(this.userId),
                com.example.board.user.domain.model.Email.of(this.email),
                this.username,
                com.example.board.user.domain.model.Password.createEncoded(this.password),
                com.example.board.user.domain.model.UserRole.valueOf(this.role.name()),
                this.createdAt,
                this.updatedAt,
                this.active
        );
    }

    // 도메인 모델에서 변환
    public static UserEntity fromDomain(com.example.board.user.domain.model.User user) {
        return new UserEntity(
                user.getUserId().getValue(),
                user.getEmail().getValue(),
                user.getUsername(),
                user.getPassword().getValue(),
                UserRole.valueOf(user.getRole().name()),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.isActive()
        );
    }

    // Getter/Setter 메서드들
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getRole() { return role; }
    public void setRole(UserRole role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    // JPA Entity용 UserRole enum
    public enum UserRole {
        ADMIN, USER
    }
}