// JwtUserPrincipal.java - 경량화된 Principal 클래스
package com.example.board.user.infrastructure.security;

import java.security.Principal;

/**
 * JWT 토큰 기반 인증을 위한 경량화된 Principal 클래스
 * UserDetails를 사용하지 않고 필요한 최소 정보만 포함
 */
public class JwtUserPrincipal implements Principal {
    private final String userId;
    private final String username;
    private final String email;
    private final boolean active;

    public JwtUserPrincipal(String userId, String username, String email, boolean active) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.active = active;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String toString() {
        return "JwtUserPrincipal{" +
                "userId='" + userId + '\'' +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}