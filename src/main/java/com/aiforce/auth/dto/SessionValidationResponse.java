package com.aiforce.auth.dto;

public record SessionValidationResponse(
        boolean valid,
        String username,
        String sessionId) {
}
