package com.aiforce.auth.client;

import com.aiforce.auth.config.TestConfig;
import com.aiforce.auth.model.LoginRequest;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class AuthApiClient {

    private final String baseUrl;

    public AuthApiClient() {
        this.baseUrl = TestConfig.baseUrl();
    }

    public Response login(String username, String password) {
        return given().baseUri(baseUrl).contentType(JSON).accept(JSON).body(new LoginRequest(username, password)).when().post("/api/auth/login");
    }

    public Response logout(String sessionId) {
        return given().baseUri(baseUrl).header("X-Session-Id", sessionId).when().post("/api/auth/logout");
    }

    public Response validateSession(String sessionId) {
        return given().baseUri(baseUrl).header("X-Session-Id", sessionId).accept(JSON).when().get("/api/auth/session/validate");
    }

    public Response accessProtected(String bearerToken) {
        return given().baseUri(baseUrl).header("Authorization", bearerToken).accept(JSON).when().get("/api/protected/account");
    }

    public Response accessProtectedWithoutAuth() {
        return given().baseUri(baseUrl).accept(JSON).when().get("/api/protected/account");
    }
}
