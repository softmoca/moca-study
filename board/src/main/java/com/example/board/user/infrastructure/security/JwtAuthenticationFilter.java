// 개선된 JwtAuthenticationFilter.java - DB 조회 없이 토큰만으로 인증
package com.example.board.user.infrastructure.security;

import com.example.board.user.infrastructure.jwt.JwtTokenService;
import com.example.board.common.exception.BusinessException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtTokenService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Authorization 헤더에서 JWT 토큰 추출
        String accessToken = extractTokenFromRequest(request);

        if (StringUtils.hasText(accessToken) && jwtTokenService.validateAccessToken(accessToken)) {
            // 2. 토큰에서 직접 정보 추출 (DB 조회 없음)
            String userId = jwtTokenService.extractUserId(accessToken);
            String username = jwtTokenService.extractUsername(accessToken);
            String email = jwtTokenService.extractEmail(accessToken);
            boolean active = jwtTokenService.isUserActiveFromToken(accessToken);
            Collection<SimpleGrantedAuthority> authorities = jwtTokenService.extractAuthorities(accessToken);

            // 3. 경량화된 Principal 객체 생성
            JwtUserPrincipal principal = new JwtUserPrincipal(userId, username, email, active);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(principal, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);


            log.debug("JWT authentication successful for user: {}", username);
        }
        // 예외 처리는 JwtExceptionFilter에서 담당

        filterChain.doFilter(request, response);
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(BEARER_PREFIX.length());
        }

        return null;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();

        // 인증이 필요 없는 경로들
        return path.startsWith("/api/auth/") ||
                path.equals("/api/users") ||
                path.startsWith("/api/public/") ||
                path.startsWith("/h2-console/") ||
                path.startsWith("/actuator/");
    }
}