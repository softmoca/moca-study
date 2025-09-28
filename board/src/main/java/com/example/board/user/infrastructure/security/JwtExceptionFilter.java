package com.example.board.user.infrastructure.security;

import com.example.board.common.exception.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            log.debug("JWT token expired: {}", e.getMessage());
            handleJwtException(response, new ExpiredTokenException("토큰이 만료되었습니다", e));
        } catch (MalformedJwtException e) {
            log.debug("Malformed JWT token: {}", e.getMessage());
            handleJwtException(response, new MalformedTokenException("잘못된 형식의 토큰입니다", e));
        } catch (UnsupportedJwtException e) {
            log.debug("Unsupported JWT token: {}", e.getMessage());
            handleJwtException(response, new UnsupportedTokenException("지원되지 않는 토큰입니다", e));
        } catch (SecurityException e) {
            log.debug("Invalid JWT signature: {}", e.getMessage());
            handleJwtException(response, new InvalidTokenSignatureException("토큰 서명이 유효하지 않습니다", e));
        } catch (JwtException e) {
            log.debug("JWT token error: {}", e.getMessage());
            handleJwtException(response, new InvalidTokenException("유효하지 않은 토큰입니다", e));
        } catch (Exception e) {
            log.error("JWT filter error: {}", e.getMessage(), e);
            handleJwtException(response, new AuthenticationProcessingException("인증 처리 중 오류가 발생했습니다", e));
        }
    }

    private void handleJwtException(HttpServletResponse response,
                                    JwtAuthenticationException exception) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        GlobalExceptionHandler.ErrorResponse errorResponse = 
            new GlobalExceptionHandler.ErrorResponse(exception.getErrorCode(), exception.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
