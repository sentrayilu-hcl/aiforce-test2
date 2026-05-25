package com.aiforce.auth.config;

public final class TestConfig {

    private TestConfig() {
    }

    public static String baseUrl() {
        return System.getenv().getOrDefault("BASE_URL", "http://localhost:8080");
    }

    public static String validUsername() {
        return System.getenv().getOrDefault("TEST_USERNAME", "customer1");
    }

    public static String validPassword() {
        return System.getenv().getOrDefault("TEST_PASSWORD", "ChangeMe123!");
    }

    public static String invalidPassword() {
        return System.getenv().getOrDefault("TEST_INVALID_PASSWORD", "WrongPassword123!");
    }

    public static long sessionWaitSeconds() {
        return Long.parseLong(System.getenv().getOrDefault("SESSION_WAIT_SECONDS", "5"));
    }
}
