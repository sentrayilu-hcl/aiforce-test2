package com.aiforce.auth.model;

public record LoginResponse(String accessToken, String tokenType, long expiresInSeconds, String sessionId) {
}
