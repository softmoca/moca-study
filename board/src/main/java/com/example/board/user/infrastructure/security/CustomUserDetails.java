// CustomUserDetails.java - Spring Security UserDetails 구현체
        package com.example.board.user.infrastructure.security;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.model.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleName = "ROLE_" + user.getRole().name();
        return Collections.singletonList(new SimpleGrantedAuthority(roleName));
    }

    @Override
    public String getPassword() {
        return user.getPassword().getValue();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    // 도메인 User 객체에 접근하는 메서드
    public User getUser() {
        return user;
    }

    public String getUserId() {
        return user.getUserId().getValue();
    }

    public String getEmail() {
        return user.getEmail().getValue();
    }

    public UserRole getRole() {
        return user.getRole();
    }
}