package com.cloud.cmr.security;

import com.cloud.cmr.authentication.web.AuthenticationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
public class WebAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void isBlockedWhenAccessingOtherThanLogin() throws Exception {
        mockMvc.perform(get("/forbidden"))
                .andExpect(status().isForbidden());
    }

    @Test
    void anybodyCanLogin() throws Exception {
        MvcResult result = mockMvc.perform(post("/login"))
                .andReturn();

        assertIsNotForbidden(result);
    }

    @Test
    @WithMockUser
    void authenticatedUserCanAccessEndpoint() throws Exception {
        MvcResult result = mockMvc.perform(post("/ok"))
                .andReturn();

        assertIsNotForbidden(result);
    }


    private void assertIsNotForbidden(MvcResult result) {
        assertThat(result.getResponse().getStatus()).isNotEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
