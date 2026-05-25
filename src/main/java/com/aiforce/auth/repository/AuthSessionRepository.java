package com.aiforce.auth.repository;

import com.aiforce.auth.domain.AuthSession;
import com.aiforce.auth.domain.SessionStatus;
import java.time.Instant;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AuthSessionRepository extends JpaRepository<AuthSession, Long> {
    Optional<AuthSession> findBySessionId(String sessionId);

    @Modifying
    @Query("update AuthSession s set s.status = :expiredStatus where s.status = :activeStatus and s.expiresAt <= :now")
    int markExpiredSessions(@Param("now") Instant now,
                            @Param("activeStatus") SessionStatus activeStatus,
                            @Param("expiredStatus") SessionStatus expiredStatus);
}
