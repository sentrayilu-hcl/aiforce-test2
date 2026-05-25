package com.aiforce.auth.security;

import com.aiforce.auth.domain.AuthSession;
import com.aiforce.auth.exception.SessionExpiredException;
import com.aiforce.auth.repository.AuthSessionRepository;
import com.aiforce.auth.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final AuthSessionRepository authSessionRepository;

    public JwtAuthenticationFilter(JwtService jwtService, AuthSessionRepository authSessionRepository) {
        this.jwtService = jwtService;
        this.authSessionRepository = authSessionRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                Claims claims = jwtService.parseClaims(token);
                String sessionId = claims.get("sid", String.class);
                AuthSession session = authSessionRepository.findBySessionId(sessionId)
                        .orElse(null);
                if (session != null && session.isActive(Instant.now())) {
                    UserDetails userDetails = User.withUsername(claims.getSubject())
                            .password("")
                            .authorities("ROLE_USER")
                            .build();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else if (session != null) {
                    throw new SessionExpiredException("Session expired");
                }
            } catch (Exception ex) {
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
