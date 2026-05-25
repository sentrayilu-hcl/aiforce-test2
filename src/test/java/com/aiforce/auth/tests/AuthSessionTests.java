package com.aiforce.auth.tests;

import com.aiforce.auth.client.AuthApiClient;
import com.aiforce.auth.config.TestConfig;
import com.aiforce.auth.model.LoginResponse;
import com.aiforce.auth.model.SessionValidationResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthSessionTests {

    private final AuthApiClient client = new AuthApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void activeSessionShouldValidateSuccessfully() throws Exception {
        Response login = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse loginResponse = mapper.readValue(login.asString(), LoginResponse.class);
        Response validation = client.validateSession(loginResponse.sessionId());
        assertThat(validation.statusCode()).isEqualTo(200);
        SessionValidationResponse response = mapper.readValue(validation.asString(), SessionValidationResponse.class);
        assertThat(response.valid()).isTrue();
        assertThat(response.username()).isEqualToIgnoringCase(TestConfig.validUsername());
        assertThat(response.sessionId()).isEqualTo(loginResponse.sessionId());
    }

    @Test
    void logoutShouldInvalidateSession() throws Exception {
        Response login = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse loginResponse = mapper.readValue(login.asString(), LoginResponse.class);
        Response logout = client.logout(loginResponse.sessionId());
        assertThat(logout.statusCode()).isEqualTo(200);
        Response validationAfterLogout = client.validateSession(loginResponse.sessionId());
        assertThat(validationAfterLogout.statusCode()).isEqualTo(401);
    }

    @Test
    void reLoginAfterLogoutShouldCreateNewSession() throws Exception {
        Response firstLogin = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse first = mapper.readValue(firstLogin.asString(), LoginResponse.class);
        Response logout = client.logout(first.sessionId());
        assertThat(logout.statusCode()).isEqualTo(200);
        Response secondLogin = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse second = mapper.readValue(secondLogin.asString(), LoginResponse.class);
        assertThat(second.sessionId()).isNotBlank();
        assertThat(second.sessionId()).isNotEqualTo(first.sessionId());
        assertThat(second.accessToken()).isNotBlank();
    }

    @Test
    void sessionWithInvalidIdShouldBeRejected() {
        Response validation = client.validateSession("invalid-session-id");
        assertThat(validation.statusCode()).isEqualTo(401);
    }
}
