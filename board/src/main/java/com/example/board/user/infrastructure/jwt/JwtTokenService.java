// JwtTokenService.java - JWT 토큰 서비스 구현체
package com.example.board.user.infrastructure.jwt;

import com.example.board.user.domain.model.*;
import com.example.board.user.domain.service.TokenService;
import com.example.board.common.exception.BusinessException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class JwtTokenService implements TokenService {

    private final SecretKey secretKey;
    private final long accessTokenValidityMinutes;
    private final long refreshTokenValidityDays;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-minutes:15}") long accessTokenValidityMinutes,
            @Value("${jwt.refresh-token-validity-days:7}") long refreshTokenValidityDays) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityMinutes = accessTokenValidityMinutes;
        this.refreshTokenValidityDays = refreshTokenValidityDays;
    }

    @Override
    public TokenPair generateTokenPair(User user) {
        AccessToken accessToken = generateAccessToken(user);
        RefreshToken refreshToken = generateRefreshToken(user);
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    public AccessToken generateAccessToken(User user) {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(accessTokenValidityMinutes);
        Date expirationDate = Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant());

        String tokenValue = Jwts.builder()
                .setSubject(user.getUserId().getValue())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("email", user.getEmail().getValue())
                .claim("username", user.getUsername())
                .claim("role", user.getRole().name())
                .claim("type", "ACCESS")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return AccessToken.of(tokenValue, expiresAt);
    }

    @Override
    public RefreshToken generateRefreshToken(User user) {
        LocalDateTime expiresAt = LocalDateTime.now().plusDays(refreshTokenValidityDays);
        Date expirationDate = Date.from(expiresAt.atZone(ZoneId.systemDefault()).toInstant());

        String tokenValue = Jwts.builder()
                .setSubject(user.getUserId().getValue())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .claim("type", "REFRESH")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return RefreshToken.of(tokenValue, expiresAt, user.getUserId());
    }

    @Override
    public boolean validateAccessToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String tokenType = claims.get("type", String.class);
            return "ACCESS".equals(tokenType);
        } catch (Exception e) {
            log.debug("Invalid access token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean validateRefreshToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String tokenType = claims.get("type", String.class);
            return "REFRESH".equals(tokenType);
        } catch (Exception e) {
            log.debug("Invalid refresh token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public UserId extractUserIdFromAccessToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String tokenType = claims.get("type", String.class);
            if (!"ACCESS".equals(tokenType)) {
                throw new BusinessException("잘못된 토큰 타입입니다");
            }
            return UserId.of(claims.getSubject());
        } catch (Exception e) {
            log.error("Failed to extract user ID from access token: {}", e.getMessage());
            throw new BusinessException("토큰에서 사용자 정보를 추출할 수 없습니다");
        }
    }

    @Override
    public UserId extractUserIdFromRefreshToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String tokenType = claims.get("type", String.class);
            if (!"REFRESH".equals(tokenType)) {
                throw new BusinessException("잘못된 토큰 타입입니다");
            }
            return UserId.of(claims.getSubject());
        } catch (Exception e) {
            log.error("Failed to extract user ID from refresh token: {}", e.getMessage());
            throw new BusinessException("토큰에서 사용자 정보를 추출할 수 없습니다");
        }
    }

    @Override
    public TokenPair refreshTokens(String refreshTokenValue) {
        if (!validateRefreshToken(refreshTokenValue)) {
            throw new BusinessException("유효하지 않은 리프레시 토큰입니다");
        }

        UserId userId = extractUserIdFromRefreshToken(refreshTokenValue);
        // 실제로는 사용자 정보를 다시 조회해야 하지만,
        // 여기서는 TokenDomainService에서 처리하도록 위임
        throw new UnsupportedOperationException("Use TokenDomainService.refreshTokens() instead");
    }

    private Claims parseToken(String tokenValue) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(tokenValue)
                .getBody();
    }

    public String extractUsernameFromToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 사용자명을 추출할 수 없습니다");
        }
    }

    public UserRole extractRoleFromToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String roleString = claims.get("role", String.class);
            return UserRole.valueOf(roleString);
        } catch (Exception e) {
            log.error("Failed to extract role from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 권한 정보를 추출할 수 없습니다");
        }
    }
}
