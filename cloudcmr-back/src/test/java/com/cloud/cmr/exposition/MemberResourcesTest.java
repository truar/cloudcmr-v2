package com.cloud.cmr.exposition;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.exposition.member.MemberDTO;
import com.cloud.cmr.exposition.member.MemberListDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.cloud.gcp.data.datastore.core.DatastoreTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

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

    private URI createMember(String lastName, String firstName, String email, String gender, String phone, String mobile) {
        String memberJson = "{" +
                "\"lastName\":\"" + lastName + "\", " +
                "\"firstName\":\"" + firstName + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"gender\":\"" + gender + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"mobile\":\"" + mobile + "\"" +
                "}";

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(memberJson, headers);
        ResponseEntity<Void> responseEntity = authenticatedRequest().
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
        URI location = createMember("Doe", "John", "john@doe.com",
                "MALE", "0401020304", "0606060606");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> authenticatedRequest().getForObject(location, MemberDTO.class) != null);

        MemberDTO member = authenticatedRequest().getForObject(location, MemberDTO.class);
        assertThat(member.lastName).isEqualTo("Doe");
        assertThat(member.firstName).isEqualTo("John");
        assertThat(member.email).isEqualTo("john@doe.com");
        assertThat(member.gender).isEqualTo("MALE");
        assertThat(member.phone).isEqualTo("0401020304");
        assertThat(member.mobile).isEqualTo("0606060606");
        assertThat(member.createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(member.creator).isEqualTo("user");
    }

    @Test
    void fetch_all_members() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
        createMember("lastName2", "firstName2", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).members.size() == 2);

        List<MemberDTO> members = authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).members;
        assertThat(members.get(0).lastName).isEqualTo("lastName1");
        assertThat(members.get(0).firstName).isEqualTo("firstName1");
        assertThat(members.get(0).email).isEqualTo("abc@def.com");
        assertThat(members.get(0).gender).isEqualTo("MALE");
        assertThat(members.get(0).phone).isEqualTo("0401020304");
        assertThat(members.get(0).mobile).isEqualTo("0606060606");
        assertThat(members.get(0).createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(members.get(0).creator).isEqualTo("user");
        assertThat(members.get(1).lastName).isEqualTo("lastName2");
        assertThat(members.get(1).firstName).isEqualTo("firstName2");
        assertThat(members.get(1).email).isEqualTo("def@ghi.fr");
        assertThat(members.get(1).gender).isEqualTo("FEMALE");
        assertThat(members.get(1).phone).isEqualTo("0102030405");
        assertThat(members.get(1).mobile).isEqualTo("0707070707");
        assertThat(members.get(1).createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(members.get(1).creator).isEqualTo("user");
    }

    private TestRestTemplate authenticatedRequest() {
        return testRestTemplate.withBasicAuth("user", "password");
    }

}
