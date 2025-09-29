package com.example.board.user.infrastructure.security;

import java.security.Principal;

public class JwtUserPrincipal implements Principal {
    private final Long userId;        // String → Long
    private final String username;
    private final String email;
    private final boolean active;

    public JwtUserPrincipal(Long userId, String username, String email, boolean active) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.active = active;
    }

    @Override
    public String getName() {
        return username;
    }

    public Long getUserId() {
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
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}