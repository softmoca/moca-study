package com.example.board.user.domain.model;

import java.util.Objects;

public class TokenPair {
    private final AccessToken accessToken;
    private final RefreshToken refreshToken;

    public TokenPair(AccessToken accessToken, RefreshToken refreshToken) {
        this.accessToken = Objects.requireNonNull(accessToken, "Access token cannot be null");
        this.refreshToken = Objects.requireNonNull(refreshToken, "Refresh token cannot be null");
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }

    public RefreshToken getRefreshToken() {
        return refreshToken;
    }

    public boolean isValid() {
        return accessToken.isValid() && refreshToken.isValid();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TokenPair tokenPair = (TokenPair) obj;
        return Objects.equals(accessToken, tokenPair.accessToken) &&
                Objects.equals(refreshToken, tokenPair.refreshToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken);
    }
}