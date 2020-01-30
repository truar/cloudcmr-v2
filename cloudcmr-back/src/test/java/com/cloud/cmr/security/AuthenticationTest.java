package com.cloud.cmr.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class AuthenticationTest {

    public static final String LOGIN = "/login";

    @Autowired
    private MockMvc mockMvc;

    @Test
    void aUserIsAbleToAuthenticateAndGetAToken() throws Exception {
        ResultActions actions = authenticateUser();
        assertUserIsAuthenticated(actions);
    }

    private ResultActions authenticateUser() throws Exception {
        return mockMvc.perform(post(LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("username", "tibo")
                .queryParam("password", "tibo"));
    }

    private void assertUserIsAuthenticated(ResultActions actions) throws Exception {
        MvcResult mvcResult = actions.andExpect(status().isOk())
                .andExpect(header().exists(AUTHORIZATION))
                .andReturn();
        String token = mvcResult.getResponse().getHeader(AUTHORIZATION);
        assertTokenIsCorrect(token);
    }

    private void assertTokenIsCorrect(String token) {
        assertNotNull(token);
    }
}
