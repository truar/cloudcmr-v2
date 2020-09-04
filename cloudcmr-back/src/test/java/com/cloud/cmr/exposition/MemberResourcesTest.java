package com.cloud.cmr.exposition;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.exposition.member.MemberDTO;
import com.cloud.cmr.exposition.member.MemberListDTO;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
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
import java.util.stream.IntStream;

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

        ResponseEntity<Void> responseEntity = postRequest("/members/create", memberJson);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI location = responseEntity.getHeaders().getLocation();
        assertThat(location).isNotNull();

        return location;
    }

    private ResponseEntity<Void> postRequest(String endpoint, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return authenticatedRequest().
                postForEntity(endpoint, entity, Void.class);
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

    @Nested
    class MembersPagination {

        @Test
        void fetch_members_with_default_pagination() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
            createMember("lastName2", "firstName2", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 2);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(2);
            List<MemberDTO> members = memberListDTO.members;
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

        @Test
        void fetch_members_with_pagination() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
            createMember("lastName2", "firstName2", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName3", "firstName3", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName4", "firstName4", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName5", "firstName5", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName6", "firstName6", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName7", "firstName7", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 7);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=1", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            List<MemberDTO> members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("lastName1");
            assertThat(members.get(0).firstName).isEqualTo("firstName1");
            assertThat(members.get(1).lastName).isEqualTo("lastName2");
            assertThat(members.get(1).firstName).isEqualTo("firstName2");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=2", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("lastName3");
            assertThat(members.get(0).firstName).isEqualTo("firstName3");
            assertThat(members.get(1).lastName).isEqualTo("lastName4");
            assertThat(members.get(1).firstName).isEqualTo("firstName4");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=3", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("lastName5");
            assertThat(members.get(0).firstName).isEqualTo("firstName5");
            assertThat(members.get(1).lastName).isEqualTo("lastName6");
            assertThat(members.get(1).firstName).isEqualTo("firstName6");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=4", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(1);
            assertThat(members.get(0).lastName).isEqualTo("lastName7");
            assertThat(members.get(0).firstName).isEqualTo("firstName7");
        }

        @Test
        void fetch_members_with_default_ascending_by_lastname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
            createMember("lastName2", "firstName2", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName3", "firstName3", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("lastName1");
            assertThat(members.get(0).firstName).isEqualTo("firstName1");
            assertThat(members.get(1).lastName).isEqualTo("lastName2");
            assertThat(members.get(1).firstName).isEqualTo("firstName2");
            assertThat(members.get(2).lastName).isEqualTo("lastName3");
            assertThat(members.get(2).firstName).isEqualTo("firstName3");
        }

        @Test
        void fetch_members_with_descending_lastname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
            createMember("lastName2", "firstName2", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName3", "firstName3", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?sortBy=lastName&sortOrder=DESC", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("lastName3");
            assertThat(members.get(0).firstName).isEqualTo("firstName3");
            assertThat(members.get(1).lastName).isEqualTo("lastName2");
            assertThat(members.get(1).firstName).isEqualTo("firstName2");
            assertThat(members.get(2).lastName).isEqualTo("lastName1");
            assertThat(members.get(2).firstName).isEqualTo("firstName1");
        }

        @Test
        void fetch_members_with_ascending_firstname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("lastName1", "firstName2", "abc@def.com", "MALE", "0401020304", "0606060606");
            createMember("lastName2", "firstName1", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");
            createMember("lastName3", "firstName3", "def@ghi.fr", "FEMALE", "0102030405", "0707070707");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?sortBy=firstName&sortOrder=ASC", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("lastName2");
            assertThat(members.get(0).firstName).isEqualTo("firstName1");
            assertThat(members.get(1).lastName).isEqualTo("lastName1");
            assertThat(members.get(1).firstName).isEqualTo("firstName2");
            assertThat(members.get(2).lastName).isEqualTo("lastName3");
            assertThat(members.get(2).firstName).isEqualTo("firstName3");
        }
    }

    @Test
    void a_user_can_change_the_address_of_a_member() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI location = createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
        String addressJson = "{" +
                "\"line1\": \"123 RUE VOLTAIRE\"," +
                "\"line2\": \"ALLEE DES TULIPES\"," +
                "\"line3\": \"LIEU-DIT\"," +
                "\"city\": \"CITY\"," +
                "\"zipCode\": \"12345\"" +
                "}";
        postRequest(location + "/changeAddress", addressJson);

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> {
                    MemberDTO memberDTO = authenticatedRequest().getForObject(location, MemberDTO.class);
                    return memberDTO != null && memberDTO.address != null;
                });

        MemberDTO member = authenticatedRequest().getForObject(location, MemberDTO.class);
        assertThat(member.address.line1).isEqualTo("123 RUE VOLTAIRE");
        assertThat(member.address.line2).isEqualTo("ALLEE DES TULIPES");
        assertThat(member.address.line3).isEqualTo("LIEU-DIT");
        assertThat(member.address.city).isEqualTo("CITY");
        assertThat(member.address.zipCode).isEqualTo("12345");

    }

    @Test
    @Disabled
    void generate_1000_members() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        IntStream.range(0, 1000).forEach(i -> {
            URI location = createMember("lastName1", "firstName1", "abc@def.com", "MALE", "0401020304", "0606060606");
//            String addressJson = "{" +
//                    "\"line1\": \"123 RUE VOLTAIRE\"," +
//                    "\"line2\": \"ALLEE DES TULIPES\"," +
//                    "\"line3\": \"LIEU-DIT\"," +
//                    "\"city\": \"CITY\"," +
//                    "\"zipCode\": \"12345\"" +
//                    "}";
//            postRequest(location + "/changeAddress", addressJson);
        });

    }

    private TestRestTemplate authenticatedRequest() {
        return testRestTemplate.withBasicAuth("user", "password");
    }

}
