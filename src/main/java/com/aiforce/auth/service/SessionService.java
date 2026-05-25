package com.aiforce.auth.service;

import com.aiforce.auth.domain.AuthSession;
import com.aiforce.auth.domain.UserAccount;
import com.aiforce.auth.dto.LoginRequest;
import com.aiforce.auth.dto.LoginResponse;
import com.aiforce.auth.dto.SessionValidationResponse;
import com.aiforce.auth.exception.AuthenticationException;
import com.aiforce.auth.exception.SessionExpiredException;
import com.aiforce.auth.exception.SessionNotFoundException;
import com.aiforce.auth.repository.AuthSessionRepository;
import com.aiforce.auth.repository.UserAccountRepository;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessionService {

    private static final Logger log = LoggerFactory.getLogger(SessionService.class);
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final UserAccountRepository userAccountRepository;
    private final AuthSessionRepository authSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Duration inactivityTimeout;

    public SessionService(UserAccountRepository userAccountRepository,
                          AuthSessionRepository authSessionRepository,
                          PasswordEncoder passwordEncoder,
                          JwtService jwtService,
                          org.springframework.core.env.Environment environment) {
        this.userAccountRepository = userAccountRepository;
        this.authSessionRepository = authSessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.inactivityTimeout = Duration.ofMinutes(Long.parseLong(environment.getProperty("security.session.timeout-minutes", "30")));
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        UserAccount userAccount = userAccountRepository.findByUsernameIgnoreCase(request.username())
                .filter(UserAccount::isEnabled)
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(request.password(), userAccount.getPasswordHash())) {
            throw new AuthenticationException("Invalid credentials");
        }

        Instant now = Instant.now();
        String sessionId = generateSessionId();
        AuthSession session = new AuthSession(sessionId, userAccount, now, now, now.plus(inactivityTimeout));
        authSessionRepository.save(session);
        String token = jwtService.generateToken(userAccount, sessionId);

        log.info("Created authenticated session for user={} sessionId={}", userAccount.getUsername(), sessionId);

        return new LoginResponse(token, "Bearer", jwtService.getJwtExpirationMillis() / 1000, sessionId);
    }

    @Transactional
    public void logout(String sessionId) {
        AuthSession session = authSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
        session.revoke(Instant.now());
        authSessionRepository.save(session);
        log.info("Revoked session sessionId={}", sessionId);
    }

    @Transactional(readOnly = true)
    public SessionValidationResponse validate(String sessionId) {
        AuthSession session = authSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));

        Instant now = Instant.now();
        if (!session.isActive(now)) {
            throw new SessionExpiredException("Session expired");
        }

        return new SessionValidationResponse(true, session.getUserAccount().getUsername(), session.getSessionId());
    }

    @Transactional
    public void touchSession(String sessionId) {
        AuthSession session = authSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException("Session not found"));
        Instant now = Instant.now();
        if (!session.isActive(now)) {
            session.expire();
            authSessionRepository.save(session);
            throw new SessionExpiredException("Session expired");
        }
        session.touch(now, now.plus(inactivityTimeout));
        authSessionRepository.save(session);
    }

    public String generateSessionId() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
