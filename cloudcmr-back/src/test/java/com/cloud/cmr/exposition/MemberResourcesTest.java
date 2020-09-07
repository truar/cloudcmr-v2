package com.cloud.cmr.exposition;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.exposition.member.MemberDTO;
import com.cloud.cmr.exposition.member.MemberListDTO;
import com.cloud.cmr.exposition.member.MemberOverviewDTO;
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
import java.time.LocalDate;
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

    private URI createMember(String lastName, String firstName, String email, String gender, String phone, String mobile, String birthDate) {
        String memberJson = "{" +
                "\"lastName\":\"" + lastName + "\", " +
                "\"firstName\":\"" + firstName + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"gender\":\"" + gender + "\"," +
                "\"phone\":\"" + phone + "\"," +
                "\"mobile\":\"" + mobile + "\"," +
                "\"birthDate\":\"" + birthDate + "\"" +
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
                "MALE", "0401020304", "0606060606", "01/01/1970");

        Awaitility.await().atMost(10, TimeUnit.SECONDS)
                .until(() -> authenticatedRequest().getForObject(location, MemberDTO.class) != null);

        MemberDTO member = authenticatedRequest().getForObject(location, MemberDTO.class);
        assertThat(member.lastName).isEqualTo("DOE");
        assertThat(member.firstName).isEqualTo("John");
        assertThat(member.email).isEqualTo("john@doe.com");
        assertThat(member.gender).isEqualTo("MALE");
        assertThat(member.phone).isEqualTo("0401020304");
        assertThat(member.mobile).isEqualTo("0606060606");
        assertThat(member.birthDate).isEqualTo(LocalDate.parse("1970-01-01"));
        assertThat(member.createdAt).isEqualTo("2020-08-28T10:00:00Z");
        assertThat(member.creator).isEqualTo("user");
    }

    @Nested
    class MembersPagination {

        @Test
        void fetch_members_with_default_pagination() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("LASTNAMEA", "FirstNameA", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
            createMember("LASTNAMEB", "FirstNameB", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 2);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(2);
            List<MemberOverviewDTO> members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEA");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameA");
            assertThat(members.get(0).email).isEqualTo("abc@def.com");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEB");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameB");
            assertThat(members.get(1).email).isEqualTo("def@ghi.fr");
        }

        @Test
        void fetch_members_with_pagination() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("LASTNAMEA", "FirstNameA", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
            createMember("LASTNAMEB", "FirstNameB", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEC", "FirstNameC", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMED", "FirstNameD", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEE", "FirstNameE", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEF", "FirstNameF", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEG", "FirstNameG", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 7);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=1", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            List<MemberOverviewDTO> members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEA");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameA");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEB");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameB");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=2", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEC");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameC");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMED");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameD");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=3", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(2);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEE");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameE");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEF");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameF");

            memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=4", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(7);
            members = memberListDTO.members;
            assertThat(members).hasSize(1);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEG");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameG");
        }

        @Test
        void fetch_members_with_default_ascending_by_lastname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("LASTNAMEA", "FirstNameA", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
            createMember("LASTNAMEB", "FirstNameB", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEC", "FirstNameC", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberOverviewDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEA");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameA");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEB");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameB");
            assertThat(members.get(2).lastName).isEqualTo("LASTNAMEC");
            assertThat(members.get(2).firstName).isEqualTo("FirstNameC");
        }

        @Test
        void fetch_members_with_descending_lastname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("LASTNAMEA", "FirstNameA", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
            createMember("LASTNAMEB", "FirstNameB", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEC", "FirstNameC", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?sortBy=lastName&sortOrder=DESC", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberOverviewDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEC");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameC");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEB");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameB");
            assertThat(members.get(2).lastName).isEqualTo("LASTNAMEA");
            assertThat(members.get(2).firstName).isEqualTo("FirstNameA");
        }

        @Test
        void fetch_members_with_ascending_firstname_sort() {
            when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
            createMember("LASTNAMEA", "FirstNameB", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
            createMember("LASTNAMEB", "FirstNameA", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");
            createMember("LASTNAMEC", "FirstNameC", "def@ghi.fr", "FEMALE", "0102030405", "0707070707", "01/01/1970");

            Awaitility.await().atMost(10, TimeUnit.SECONDS)
                    .until(() -> authenticatedRequest().getForObject(MEMBERS, MemberListDTO.class).total == 3);

            MemberListDTO memberListDTO = authenticatedRequest().getForObject(MEMBERS + "?sortBy=firstName&sortOrder=ASC", MemberListDTO.class);
            assertThat(memberListDTO.total).isEqualTo(3);
            List<MemberOverviewDTO> members = memberListDTO.members;
            assertThat(members).hasSize(3);
            assertThat(members.get(0).lastName).isEqualTo("LASTNAMEB");
            assertThat(members.get(0).firstName).isEqualTo("FirstNameA");
            assertThat(members.get(1).lastName).isEqualTo("LASTNAMEA");
            assertThat(members.get(1).firstName).isEqualTo("FirstNameB");
            assertThat(members.get(2).lastName).isEqualTo("LASTNAMEC");
            assertThat(members.get(2).firstName).isEqualTo("FirstNameC");
        }
    }

    @Test
    void a_user_can_change_the_address_of_a_member() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI location = createMember("LASTNAME", "FirstName", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
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
            URI location = createMember("LASTNAMEA", "FirstNameA", "abc@def.com", "MALE", "0401020304", "0606060606", "01/01/1970");
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
