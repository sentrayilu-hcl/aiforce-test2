package com.aiforce.auth.model;

public record SessionValidationResponse(boolean valid, String username, String sessionId) {
}
