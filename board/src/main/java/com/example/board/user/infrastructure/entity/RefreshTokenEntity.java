package com.example.board.user.infrastructure.entity;

import com.example.board.user.domain.model.RefreshToken;
import com.example.board.user.domain.model.UserId;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "token_value", unique = true, nullable = false, length = 512)
    private String tokenValue;

    @Column(name = "user_public_id", nullable = false)
    private String userPublicId;  // UserId의 값

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public RefreshTokenEntity(String tokenValue, String userPublicId,
                              LocalDateTime expiresAt, LocalDateTime createdAt) {
        this.tokenValue = tokenValue;
        this.userPublicId = userPublicId;
        this.expiresAt = expiresAt;
        this.createdAt = createdAt;
    }

    // 도메인 모델로 변환
    public RefreshToken toDomain() {
        return RefreshToken.of(
                this.tokenValue,
                this.expiresAt,
                UserId.of(this.userPublicId)
        );
    }

    // 도메인 모델에서 변환
    public static RefreshTokenEntity fromDomain(RefreshToken refreshToken) {
        return new RefreshTokenEntity(
                refreshToken.getValue(),
                refreshToken.getUserId().getValue(),
                refreshToken.getExpiresAt(),
                LocalDateTime.now()
        );
    }
}