package com.example.board.user.infrastructure.jwt;

import com.example.board.user.domain.model.*;
import com.example.board.user.domain.service.TokenService;
import com.example.board.common.exception.BusinessException;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class JwtTokenService implements TokenService {

    private final SecretKey secretKey;
    private final long accessTokenValidityMinutes;

    public JwtTokenService(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-minutes:60}") long accessTokenValidityMinutes) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTokenValidityMinutes = accessTokenValidityMinutes;
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
                .claim("role", "ROLE_" + user.getRole().name()) // Spring Security 표준 형태
                .claim("active", user.isActive())
                .claim("type", "ACCESS")
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return AccessToken.of(tokenValue, expiresAt);
    }

    @Override
    public boolean validateAccessToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String tokenType = claims.get("type", String.class);
            Boolean isActive = claims.get("active", Boolean.class);

            return "ACCESS".equals(tokenType) && Boolean.TRUE.equals(isActive);
        } catch (Exception e) {
            log.debug("Invalid access token: {}", e.getMessage());
            return false;
        }
    }

    // 토큰에서 사용자 ID 추출 (DB 조회 없음)
    public String extractUserId(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            return claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to extract user ID from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 사용자 정보를 추출할 수 없습니다");
        }
    }

    // 토큰에서 사용자명 추출 (DB 조회 없음)
    public String extractUsername(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            return claims.get("username", String.class);
        } catch (Exception e) {
            log.error("Failed to extract username from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 사용자명을 추출할 수 없습니다");
        }
    }

    // 토큰에서 이메일 추출 (DB 조회 없음)
    public String extractEmail(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            return claims.get("email", String.class);
        } catch (Exception e) {
            log.error("Failed to extract email from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 이메일을 추출할 수 없습니다");
        }
    }

    // 토큰에서 권한 추출 (DB 조회 없음)
    public Collection<SimpleGrantedAuthority> extractAuthorities(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            String role = claims.get("role", String.class);
            return List.of(new SimpleGrantedAuthority(role));
        } catch (Exception e) {
            log.error("Failed to extract authorities from token: {}", e.getMessage());
            throw new BusinessException("토큰에서 권한 정보를 추출할 수 없습니다");
        }
    }

    // 토큰에서 활성 상태 확인 (DB 조회 없음)
    public boolean isUserActiveFromToken(String tokenValue) {
        try {
            Claims claims = parseToken(tokenValue);
            return claims.get("active", Boolean.class);
        } catch (Exception e) {
            log.error("Failed to extract active status from token: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public UserId extractUserIdFromAccessToken(String tokenValue) {
        return UserId.of(extractUserId(tokenValue));
    }

    private Claims parseToken(String tokenValue) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(tokenValue)
                .getBody();
    }
}