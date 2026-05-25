package com.aiforce.auth.tests;

import com.aiforce.auth.client.AuthApiClient;
import com.aiforce.auth.config.TestConfig;
import com.aiforce.auth.model.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthLoginTests {

    private final AuthApiClient client = new AuthApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void validCredentialsShouldCreateAuthenticatedSession() throws Exception {
        Response response = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        assertThat(response.statusCode()).isEqualTo(200);
        LoginResponse loginResponse = mapper.readValue(response.asString(), LoginResponse.class);
        assertThat(loginResponse.accessToken()).isNotBlank();
        assertThat(loginResponse.tokenType()).isEqualToIgnoringCase("Bearer");
        assertThat(loginResponse.sessionId()).isNotBlank();
        assertThat(loginResponse.expiresInSeconds()).isPositive();
    }

    @Test
    void invalidCredentialsShouldBeRejected() {
        Response response = client.login(TestConfig.validUsername(), TestConfig.invalidPassword());
        assertThat(response.statusCode()).isEqualTo(401);
        assertThat(response.asString()).containsIgnoringCase("invalid credentials");
    }
}
