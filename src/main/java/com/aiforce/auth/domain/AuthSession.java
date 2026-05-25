package com.aiforce.auth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "auth_session")
public class AuthSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "session_id", nullable = false, unique = true, length = 64)
    private String sessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccount userAccount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private SessionStatus status;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "last_accessed_at", nullable = false)
    private Instant lastAccessedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    protected AuthSession() {
    }

    public AuthSession(String sessionId, UserAccount userAccount, Instant issuedAt, Instant lastAccessedAt, Instant expiresAt) {
        this.sessionId = sessionId;
        this.userAccount = userAccount;
        this.status = SessionStatus.ACTIVE;
        this.issuedAt = issuedAt;
        this.lastAccessedAt = lastAccessedAt;
        this.expiresAt = expiresAt;
    }

    public Long getId() {
        return id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public Instant getIssuedAt() {
        return issuedAt;
    }

    public Instant getLastAccessedAt() {
        return lastAccessedAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public Instant getRevokedAt() {
        return revokedAt;
    }

    public void touch(Instant now, Instant newExpiresAt) {
        this.lastAccessedAt = now;
        this.expiresAt = newExpiresAt;
    }

    public void revoke(Instant revokedAt) {
        this.status = SessionStatus.REVOKED;
        this.revokedAt = revokedAt;
    }

    public boolean isActive(Instant now) {
        return status == SessionStatus.ACTIVE && expiresAt.isAfter(now);
    }

    public void expire() {
        this.status = SessionStatus.EXPIRED;
    }
}
