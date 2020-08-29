package com.cloud.cmr.exposition;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.exposition.member.MemberDTO;
import com.cloud.cmr.exposition.member.MemberListDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class MemberResourcesTest {

    public static final String MEMBERS = "/members";

    @Autowired
    private DatastoreTemplate datastoreTemplate;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        datastoreTemplate.deleteAll(Member.class);
    }

    private URI createMember(String lastName, String firstName, String email) {
        String memberJson = "{" +
                "\"lastName\":\"" + lastName + "\", " +
                "\"firstName\":\"" + firstName + "\"," +
                "\"email\":\"" + email + "\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(memberJson, headers);
        ResponseEntity<Void> responseEntity = authRequest().
                postForEntity("/members/create", entity, Void.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI location = responseEntity.getHeaders().getLocation();
        assertThat(location).isNotNull();

        return location;
    }

    @Test
    void only_authenticated_user_can_access_members_api() {
        testRestTemplate.getForObject(MEMBERS, Void.class);
    }

    @Test
    void create_a_new_member_and_fetch_it() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI location = createMember("Doe", "John", "john@doe.com");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                MemberDTO member = authRequest().getForObject(location, MemberDTO.class);
                assertThat(member.lastName).isEqualTo("Doe");
                assertThat(member.firstName).isEqualTo("John");
                assertThat(member.email).isEqualTo("john@doe.com");
                assertThat(member.createdAt).isEqualTo("2020-08-28T10:00:00Z");
                assertThat(member.creator).isEqualTo("user");
            });
    }

    private TestRestTemplate authRequest() {
        return testRestTemplate.withBasicAuth("user", "password");
    }

    @Test
    void fetch_all_members() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        createMember("lastName1", "firstName1", "abc@def.com");
        createMember("lastName2", "firstName2", "def@ghi.fr");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> authRequest().getForObject(MEMBERS, MemberListDTO.class).members.size() == 2);

        List<MemberDTO> members = authRequest().getForObject(MEMBERS, MemberListDTO.class).members;
        assertThat(members.get(0).lastName).isEqualTo("lastName1");
        assertThat(members.get(0).firstName).isEqualTo("firstName1");
        assertThat(members.get(0).email).isEqualTo("abc@def.com");
        assertThat(members.get(0).createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(members.get(0).creator).isEqualTo("user");
        assertThat(members.get(1).lastName).isEqualTo("lastName2");
        assertThat(members.get(1).firstName).isEqualTo("firstName2");
        assertThat(members.get(1).email).isEqualTo("def@ghi.fr");
        assertThat(members.get(1).createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(members.get(1).creator).isEqualTo("user");
    }

    @Test
    @Transactional
    @WithMockUser
    @Disabled
    void update_a_member_and_fetch_the_new_value() throws Exception {
//        MvcResult memberResponse = createMember("lastName1", "firstName1", "abc@def.com");
//        String location = memberResponse.getResponse().getHeader(LOCATION);
//
        String updatedMember = "{" +
                "\"lastName\":\"Doe\", " +
                "\"firstName\":\"John\"," +
                "\"email\":\"john@doe.com\"" +
                "}";

//        mockMvc.perform(put(location)
//                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                .content(updatedMember))
//                .andExpect(status().isNoContent());
//
//        mockMvc.perform(get(location))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.lastName", equalTo("Doe")))
//                .andExpect(jsonPath("$.firstName", equalTo("John")))
//                .andExpect(jsonPath("$.email", equalTo("john@doe.com")));

    }

    @Test
    @Transactional
    @WithMockUser
    @Disabled
    void delete_a_member() throws Exception {
//        MvcResult memberResponse = createMember("Doe", "John", "john@doe.com");
//        String location = memberResponse.getResponse().getHeader(LOCATION);

//        mockMvc.perform(delete(location))
//                .andExpect(status().isNoContent());
//        mockMvc.perform(get(location))
//                .andExpect(status().isNotFound());
    }
}
