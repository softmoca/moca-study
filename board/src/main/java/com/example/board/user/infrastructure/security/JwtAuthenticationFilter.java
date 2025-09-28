package com.example.board.user.infrastructure.security;

import com.example.board.user.domain.model.User;
import com.example.board.user.domain.service.TokenDomainService;
import com.example.board.common.exception.BusinessException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final TokenDomainService tokenDomainService;
    private final CustomUserDetailsService userDetailsService;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = extractTokenFromRequest(request);

            if (StringUtils.hasText(accessToken) && tokenDomainService.isAccessTokenValid(accessToken)) {
                User user = tokenDomainService.validateTokenAndGetUser(accessToken);
                UserDetails userDetails = userDetailsService.createUserDetails(user);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 사용자 ID를 요청 속성에 저장 (컨트롤러에서 사용 가능)
                request.setAttribute("userId", user.getUserId().getValue());
            }
        } catch (BusinessException e) {
            log.debug("JWT authentication failed: {}", e.getMessage());
            // 인증 실패시 SecurityContext를 clear하고 계속 진행
            SecurityContextHolder.clearContext();
        } catch (Exception e) {
            log.error("JWT authentication error: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
        }

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
