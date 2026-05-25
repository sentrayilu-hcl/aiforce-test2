package com.aiforce.auth.controller;

import com.aiforce.auth.dto.LoginRequest;
import com.aiforce.auth.dto.LoginResponse;
import com.aiforce.auth.dto.LogoutResponse;
import com.aiforce.auth.dto.SessionValidationResponse;
import com.aiforce.auth.service.SessionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final SessionService sessionService;

    public AuthController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(sessionService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("X-Session-Id") String sessionId) {
        sessionService.logout(sessionId);
        return ResponseEntity.ok(new LogoutResponse("Logged out successfully"));
    }

    @GetMapping("/session/validate")
    public ResponseEntity<SessionValidationResponse> validate(@RequestHeader("X-Session-Id") String sessionId) {
        return ResponseEntity.ok(sessionService.validate(sessionId));
    }
}
