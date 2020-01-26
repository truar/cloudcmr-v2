package com.cloud.cmr.authentication;

import com.cloud.cmr.authentication.web.AuthenticationController;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerTest {

    public static final String LOGIN = "/login";
    public static final String AUTH_TOKEN = "authToken";
    public static final String MAX_AGE = "Max-Age";
    public static final String HTTP_ONLY = "HttpOnly";
    @Value("${cookie.duration.in.second}")
    public int cookieDuration;

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void aUserIsAbleToAuthenticateAndGetACookie() throws Exception {
        ResultActions actions = authenticateUser();
        assertUserIsAuthenticated(actions);
    }

    private ResultActions authenticateUser() throws Exception {
        UserCredentials userCredentials = new UserCredentials("user", "user");
        String requestBody = objectMapper.writeValueAsString(userCredentials);
        return mockMvc.perform(post(LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody));
    }

    private void assertUserIsAuthenticated(ResultActions actions) throws Exception {
        MvcResult mvcResult = actions.andExpect(status().isNoContent())
                .andExpect(header().exists(SET_COOKIE))
                .andReturn();
        String cookie = mvcResult.getResponse().getHeader(SET_COOKIE);
        assertCookieIsCorrect(cookie);
    }

    private void assertCookieIsCorrect(String cookie) {
        Map<String, String> keyValueCookie = parseCookieAsMap(cookie);

        assertAll("Cookie contains good value",
                () -> assertEquals("CHANGEME", keyValueCookie.get(AUTH_TOKEN)),
                () -> assertEquals("3600", keyValueCookie.get(MAX_AGE)),
                () -> assertTrue(keyValueCookie.containsKey(HTTP_ONLY))
        );
    }

    private Map<String, String> parseCookieAsMap(String cookie) {
        return Arrays.stream(cookie.split("; "))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(a -> a[0], a -> a.length > 1 ? a[1] : ""));
    }
}
