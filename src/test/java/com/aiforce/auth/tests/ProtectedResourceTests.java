package com.aiforce.auth.tests;

import com.aiforce.auth.client.AuthApiClient;
import com.aiforce.auth.config.TestConfig;
import com.aiforce.auth.model.LoginResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProtectedResourceTests {

    private final AuthApiClient client = new AuthApiClient();
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void protectedEndpointShouldRejectMissingAuthorizationHeader() {
        Response response = client.accessProtectedWithoutAuth();
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void protectedEndpointShouldRejectMalformedAuthorizationHeader() {
        Response response = client.accessProtected("InvalidTokenValue");
        assertThat(response.statusCode()).isEqualTo(401);
    }

    @Test
    void protectedEndpointShouldAllowValidToken() throws Exception {
        Response login = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse loginResponse = mapper.readValue(login.asString(), LoginResponse.class);
        Response protectedResponse = client.accessProtected("Bearer " + loginResponse.accessToken());
        assertThat(protectedResponse.statusCode()).isEqualTo(200);
        assertThat(protectedResponse.asString()).containsIgnoringCase("Protected account features");
    }

    @Test
    void expiredOrRevokedSessionShouldRequireReAuthentication() throws Exception {
        Response login = client.login(TestConfig.validUsername(), TestConfig.validPassword());
        LoginResponse loginResponse = mapper.readValue(login.asString(), LoginResponse.class);
        Response logout = client.logout(loginResponse.sessionId());
        assertThat(logout.statusCode()).isEqualTo(200);
        Response protectedResponse = client.accessProtected("Bearer " + loginResponse.accessToken());
        assertThat(protectedResponse.statusCode()).isEqualTo(401);
    }
}
