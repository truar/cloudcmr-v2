package com.cloud.cmr.exposition;

import com.cloud.cmr.domain.member.Gender;
import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.MemberRepository;
import com.cloud.cmr.domain.member.PhoneNumber;
import com.cloud.cmr.exposition.member.MemberDTO;
import com.jayway.jsonpath.JsonPath;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class MemberResourcesTest {

    public static final String MEMBERS = "/members";

    @Autowired
    private DatastoreTemplate datastoreTemplate;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private Clock clock;

    @BeforeEach
    void setUp() {
        datastoreTemplate.deleteAll(Member.class);
    }

    @Test
    void get_unknown_member_response_404() {
        String unknownMember = "memberId";
        String location = "/members/" + unknownMember;

        ResponseEntity<String> responseEntity = authenticatedRequest().getForEntity(location, String.class);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        String body = responseEntity.getBody();
        with(body).assertThat("$.status", equalTo(404));
        with(body).assertThat("$.message", equalTo("No member found with id: memberId"));
    }

    @Test
    void only_authenticated_user_can_access_members_api() {
        testRestTemplate.getForObject(MEMBERS, Void.class);
    }

    @Nested
    class MembersPagination {

        @Test
        void fetch_members_with_default_pagination() {
            memberRepository.save(new Member("1", "LASTNAMEA", "FirstNameA", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("2", "LASTNAMEB", "FirstNameB", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                    .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject(MEMBERS, String.class)).read("$.total") == 2);

            String response = authenticatedRequest().getForObject(MEMBERS, String.class);
            with(response).assertThat("$.total", equalTo(2));
            with(response).assertThat("$.members", hasSize(2));
            with(response).assertThat("$.members[0].id", equalTo("1"));
            with(response).assertThat("$.members[1].id", equalTo("2"));
        }

        @Test
        void fetch_members_with_pagination() {
            memberRepository.save(new Member("1", "LASTNAMEA", "FirstNameA", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("2", "LASTNAMEB", "FirstNameB", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("3", "LASTNAMEC", "FirstNameC", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                    .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject(MEMBERS, String.class)).read("$.total") == 3);

            String response = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=1", String.class);
            with(response).assertThat("$.total", equalTo(3));
            with(response).assertThat("$.members", hasSize(2));
            with(response).assertThat("$.members[0].id", equalTo("1"));
            with(response).assertThat("$.members[1].id", equalTo("2"));

            response = authenticatedRequest().getForObject(MEMBERS + "?pageSize=2&page=2", String.class);
            with(response).assertThat("$.total", equalTo(3));
            with(response).assertThat("$.members", hasSize(1));
            with(response).assertThat("$.members[0].id", equalTo("3"));
        }

        @Test
        void fetch_members_with_default_ascending_by_lastname_sort() {
            memberRepository.save(new Member("1", "B", "FirstNameA", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("2", "A", "FirstNameB", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("3", "C", "FirstNameC", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                    .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject(MEMBERS, String.class)).read("$.total") == 3);

            String response = authenticatedRequest().getForObject(MEMBERS, String.class);
            with(response).assertThat("$.total", equalTo(3));
            with(response).assertThat("$.members", hasSize(3));
            with(response).assertThat("$.members[0].lastName", equalTo("A"));
            with(response).assertThat("$.members[1].lastName", equalTo("B"));
            with(response).assertThat("$.members[2].lastName", equalTo("C"));
        }

        @Test
        void fetch_members_with_descending_lastname_sort() {
            memberRepository.save(new Member("1", "B", "FirstNameA", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("2", "A", "FirstNameB", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("3", "C", "FirstNameC", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                    .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject(MEMBERS, String.class)).read("$.total") == 3);

            String response = authenticatedRequest().getForObject(MEMBERS + "?sortBy=lastName&sortOrder=DESC", String.class);
            with(response).assertThat("$.total", equalTo(3));
            with(response).assertThat("$.members", hasSize(3));
            with(response).assertThat("$.members[0].lastName", equalTo("C"));
            with(response).assertThat("$.members[1].lastName", equalTo("B"));
            with(response).assertThat("$.members[2].lastName", equalTo("A"));
        }

        @Test
        void fetch_members_with_ascending_firstname_sort() {
            memberRepository.save(new Member("1", "B", "E", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("2", "A", "F", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));
            memberRepository.save(new Member("3", "C", "G", "abc@def.com",
                    LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                    new PhoneNumber("0606060606"), "user", clock.instant()));

            Awaitility.await().atMost(5, TimeUnit.SECONDS)
                    .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject(MEMBERS, String.class)).read("$.total") == 3);

            String response = authenticatedRequest().getForObject(MEMBERS + "?sortBy=firstName&sortOrder=DESC", String.class);
            with(response).assertThat("$.total", equalTo(3));
            with(response).assertThat("$.members", hasSize(3));
            with(response).assertThat("$.members[0].firstName", equalTo("G"));
            with(response).assertThat("$.members[1].firstName", equalTo("F"));
            with(response).assertThat("$.members[2].firstName", equalTo("E"));
        }
    }

    private TestRestTemplate authenticatedRequest() {
        return testRestTemplate.withBasicAuth("user", "password");
    }

}
