package com.cloud.cmr.exposition;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberResourcesTest {

    public static final String MEMBERS = "/members";

    @Autowired
    private MockMvc mockMvc;

    private MvcResult createMember(String lastName, String firstName, String email) throws Exception {
        String memberAsString = "{" +
                "\"lastName\":\"" + lastName + "\", " +
                "\"firstName\":\"" + firstName + "\"," +
                "\"email\":\"" + email + "\"" +
                "}";

        return mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(memberAsString))
                .andExpect(status().isCreated())
                .andExpect(header().exists(LOCATION))
                .andReturn();
    }

    @Test
    void only_authenticated_user_can_access_members_api() throws Exception {
        mockMvc.perform(get(MEMBERS))
                .andExpect(status().isForbidden());

    }

    @Test
    @Transactional
    @WithMockUser
    void create_a_new_member_and_fetch_it() throws Exception {
        MvcResult postResponse = createMember("Doe", "John", "john@doe.com");

        String location = postResponse.getResponse().getHeader(LOCATION);

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", equalTo("Doe")))
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.email", equalTo("john@doe.com")));


    }

    @Test
    @Transactional
    @WithMockUser
    void fetch_all_members() throws Exception {
        createMember("lastName1", "firstName1", "abc@def.com");
        createMember("lastName2", "firstName2", "def@ghi.fr");

        mockMvc.perform(get(MEMBERS))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.members", hasSize(2)))
                .andExpect(jsonPath("$._embedded.members[0].lastName", equalTo("lastName1")))
                .andExpect(jsonPath("$._embedded.members[0].firstName", equalTo("firstName1")))
                .andExpect(jsonPath("$._embedded.members[0].email", equalTo("abc@def.com")))
                .andExpect(jsonPath("$._embedded.members[1].lastName", equalTo("lastName2")))
                .andExpect(jsonPath("$._embedded.members[1].firstName", equalTo("firstName2")))
                .andExpect(jsonPath("$._embedded.members[1].email", equalTo("def@ghi.fr")))
        ;
    }

    @Test
    @Transactional
    @WithMockUser
    void update_a_member_and_fetch_the_new_value() throws Exception {
        MvcResult memberResponse = createMember("lastName1", "firstName1", "abc@def.com");
        String location = memberResponse.getResponse().getHeader(LOCATION);

        String updatedMember = "{" +
                "\"lastName\":\"Doe\", " +
                "\"firstName\":\"John\"," +
                "\"email\":\"john@doe.com\"" +
                "}";

        mockMvc.perform(put(location)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updatedMember))
                .andExpect(status().isNoContent());

        mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", equalTo("Doe")))
                .andExpect(jsonPath("$.firstName", equalTo("John")))
                .andExpect(jsonPath("$.email", equalTo("john@doe.com")));

    }

    @Test
    @Transactional
    @WithMockUser
    void delete_a_member() throws Exception {
        MvcResult memberResponse = createMember("Doe", "John", "john@doe.com");
        String location = memberResponse.getResponse().getHeader(LOCATION);

        mockMvc.perform(delete(location))
                .andExpect(status().isNoContent());
        mockMvc.perform(get(location))
                .andExpect(status().isNotFound());
    }
}
