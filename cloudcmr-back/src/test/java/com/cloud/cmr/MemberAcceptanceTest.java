package com.cloud.cmr;

import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.exposition.member.Body;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
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
import java.util.Base64;
import java.util.concurrent.TimeUnit;

import static com.jayway.jsonassert.JsonAssert.with;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
public class MemberAcceptanceTest {

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

    @Test
    void create_a_new_member_and_fetch_it() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI location = createMember("Doe", "John", "john@doe.com", "MALE", "0606060606", "01/01/1970");

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until(() -> authenticatedRequest().getForObject(location, String.class) != null);

        String response = authenticatedRequest().getForObject(location, String.class);
        with(response).assertThat("$.lastName", equalTo("DOE"));
        with(response).assertThat("$.firstName", equalTo("John"));
        with(response).assertThat("$.email", equalTo("john@doe.com"));
        with(response).assertThat("$.gender", equalTo("MALE"));
        with(response).assertThat("$.phone", equalTo(null));
        with(response).assertThat("$.mobile", equalTo("06 06 06 06 06"));
        with(response).assertThat("$.birthDate", equalTo("01/01/1970"));
        with(response).assertThat("$.createdAt", equalTo("2020-08-28T10:00:00Z"));
        with(response).assertThat("$.creator", equalTo("user"));
        with(response).assertThat("$.address", equalTo(null));
    }

    @Test
    void change_the_address_after_the_member_is_created() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI memberLocation = createMember("Doe", "John", "john@doe.com", "MALE", "0606060606", "01/01/1970");

        String addressJson = "{" +
                "\"line1\": \"123 RUE VOLTAIRE\"," +
                "\"line2\": \"ALLEE DES TULIPES\"," +
                "\"line3\": \"LIEU-DIT\"," +
                "\"city\": \"CITY\"," +
                "\"zipCode\": \"12345\"" +
                "}";
        postRequest(memberLocation + "/changeAddress", addressJson);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until(() -> {
                    String response = authenticatedRequest().getForObject(memberLocation, String.class);
                    return JsonPath.parse(response).read("$.address") != null;
                });

        String response = authenticatedRequest().getForObject(memberLocation, String.class);
        with(response).assertThat("$.lastName", equalTo("DOE"));
        with(response).assertThat("$.firstName", equalTo("John"));
        with(response).assertThat("$.email", equalTo("john@doe.com"));
        with(response).assertThat("$.gender", equalTo("MALE"));
        with(response).assertThat("$.phone", equalTo(null));
        with(response).assertThat("$.mobile", equalTo("06 06 06 06 06"));
        with(response).assertThat("$.birthDate", equalTo("01/01/1970"));
        with(response).assertThat("$.createdAt", equalTo("2020-08-28T10:00:00Z"));
        with(response).assertThat("$.creator", equalTo("user"));
        with(response).assertThat("$.address.line1", equalTo("123 RUE VOLTAIRE"));
        with(response).assertThat("$.address.line2", equalTo("ALLEE DES TULIPES"));
        with(response).assertThat("$.address.line3", equalTo("LIEU-DIT"));
        with(response).assertThat("$.address.city", equalTo("CITY"));
        with(response).assertThat("$.address.zipCode", equalTo("12345"));
    }

    @Test
    void change_some_contact_information_after_the_member_is_created() {
        when(clock.instant()).thenReturn(Instant.parse("2020-08-28T10:00:00Z"));
        URI memberLocation = createMember("Doe", "John", "john@doe.com", "MALE", "0606060606", "01/01/1970");
        String contactInformationJson = "{" +
                "\"lastName\":\"doe\", " +
                "\"firstName\":\"John\"," +
                "\"email\":\"john@doe.com\"," +
                "\"gender\":\"MALE\"," +
                "\"phone\":\"0450020304\"," +
                "\"mobile\":\"0606060606\"," +
                "\"birthDate\":\"01/01/1970\"" +
                "}";
        postRequest(memberLocation + "/changeContactInformation", contactInformationJson);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until(() -> {
                    String response = authenticatedRequest().getForObject(memberLocation, String.class);
                    return JsonPath.parse(response).read("$.phone") != null;
                });

        String response = authenticatedRequest().getForObject(memberLocation, String.class);
        with(response).assertThat("$.lastName", equalTo("DOE"));
        with(response).assertThat("$.firstName", equalTo("John"));
        with(response).assertThat("$.email", equalTo("john@doe.com"));
        with(response).assertThat("$.gender", equalTo("MALE"));
        with(response).assertThat("$.phone", equalTo("04 50 02 03 04"));
        with(response).assertThat("$.mobile", equalTo("06 06 06 06 06"));
        with(response).assertThat("$.birthDate", equalTo("01/01/1970"));
        with(response).assertThat("$.createdAt", equalTo("2020-08-28T10:00:00Z"));
        with(response).assertThat("$.creator", equalTo("user"));
    }

    @Test
    void fetch_members_while_applying_filtering_and_sorting_request() {
        createMember("B", "E", "abc@def.com", "MALE", "0601020304", "01/01/1970");
        createMember("A", "F", "abc@def.com", "MALE", "0601020304", "01/01/1970");
        createMember("C", "G", "abc@def.com", "MALE", "0601020304", "01/01/1970");

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject("/members", String.class)).read("$.total") == 3);

        String response = authenticatedRequest().getForObject("/members" + "?page=1&pageSize=2&sortBy=firstName&sortOrder=DESC", String.class);
        with(response).assertThat("$.total", equalTo(3));
        with(response).assertThat("$.members", hasSize(2));
        with(response).assertThat("$.members[0].firstName", equalTo("G"));
        with(response).assertThat("$.members[1].firstName", equalTo("F"));

        response = authenticatedRequest().getForObject("/members" + "?page=2&pageSize=2&sortBy=firstName&sortOrder=DESC", String.class);
        with(response).assertThat("$.total", equalTo(3));
        with(response).assertThat("$.members", hasSize(1));
        with(response).assertThat("$.members[0].firstName", equalTo("E"));
    }

    @Test
    void can_import_twice_the_same_member() throws JsonProcessingException {
        String data = "{\"licenceNumber\":\"PC11737992\",\"firstName\":\"john\",\"lastName\":\"doe\",\"gender\":\"M\",\"birthDate\":\"1967-01-01\",\"email\":\"john.doe@mail.fr\",\"phone\":\"\",\"mobile\":\"\",\"line1\":\"1 chemin de la localisation\",\"line2\":\"\",\"line3\":\"Lieu-Dit\",\"zipCode\":\"12345\",\"city\":\"Ma Ville\"}";
        String encodedData = Base64.getEncoder().encodeToString(data.getBytes());
        Body body = new Body();
        body.setMessage(new Body.Message("1", "", encodedData));
        String content = new ObjectMapper().writeValueAsString(body);
        ResponseEntity<Void> voidResponseEntity = postRequest("/members/import", content);
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        voidResponseEntity = postRequest("/members/import", content);
        assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        Awaitility.await().atMost(5, TimeUnit.SECONDS)
                .until(() -> (int) JsonPath.parse(authenticatedRequest().getForObject("/members", String.class)).read("$.total") == 1);

        String response = authenticatedRequest().getForObject("/members", String.class);
        with(response).assertThat("$.total", equalTo(1));
        with(response).assertThat("$.members", hasSize(1));
        with(response).assertThat("$.members[0].firstName", equalTo("John"));
        with(response).assertThat("$.members[0].lastName", equalTo("DOE"));
    }

    private URI createMember(String lastName, String firstName, String email, String gender, String mobile, String birthDate) {
        String memberJson = "{" +
                "\"lastName\":\"" + lastName + "\", " +
                "\"firstName\":\"" + firstName + "\"," +
                "\"email\":\"" + email + "\"," +
                "\"gender\":\"" + gender + "\"," +
                "\"mobile\":\"" + mobile + "\"," +
                "\"birthDate\":\"" + birthDate + "\"" +
                "}";

        ResponseEntity<Void> responseEntity = postRequest("/members/create", memberJson);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        URI memberLocation = responseEntity.getHeaders().getLocation();
        assertThat(memberLocation).isNotNull();
        return memberLocation;
    }

    private ResponseEntity<Void> postRequest(String endpoint, String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);
        return authenticatedRequest().
                postForEntity(endpoint, entity, Void.class);
    }

    private TestRestTemplate authenticatedRequest() {
        return testRestTemplate.withBasicAuth("user", "password");
    }

}
