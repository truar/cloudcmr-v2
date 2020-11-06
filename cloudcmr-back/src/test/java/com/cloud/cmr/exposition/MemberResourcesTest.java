package com.cloud.cmr.exposition;

import com.cloud.cmr.application.member.MemberManager;
import com.cloud.cmr.domain.common.Page;
import com.cloud.cmr.domain.member.Gender;
import com.cloud.cmr.domain.member.Member;
import com.cloud.cmr.domain.member.PhoneNumber;
import com.cloud.cmr.exposition.member.Body;
import com.cloud.cmr.exposition.member.MemberResources;
import com.cloud.cmr.exposition.member.MemberResourcesAdvice;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({MemberResources.class, Base64AuthenticationTokenFilter.class, MemberResourcesAdvice.class})
@ActiveProfiles("test")
public class MemberResourcesTest {

    public static final String MEMBERS = "/members";
    public static final String BASE64_TOKEN = "Basic " + Base64Utils.encodeToString("user:password".getBytes());

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MemberManager service;

    @Test
    void get_unknown_member_response_404() throws Exception {
        String unknownMember = "memberId";
        when(service.memberOfId(unknownMember)).thenThrow(new NoSuchElementException());

        mockMvc.perform(get("/members/" + unknownMember)
                .header(AUTHORIZATION, BASE64_TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No member found with id: memberId"));

        verify(service).memberOfId("memberId");
    }

    @Test
    void error_404_when_changing_address_of_an_unknown_member() throws Exception {
        String unknownMember = "memberId";
        doThrow(new NoSuchElementException())
                .when(service)
                .changeMemberAddress(unknownMember, "", "", "", "", "");

        String request = "{" +
                "\"line1\": \"\"," +
                "\"line2\": \"\"," +
                "\"line3\": \"\"," +
                "\"city\": \"\"," +
                "\"zipCode\": \"\"" +
                "}";
        mockMvc.perform(post("/members/" + unknownMember + "/changeAddress")
                .header(AUTHORIZATION, BASE64_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No member found with id: memberId"));

        verify(service).changeMemberAddress(unknownMember, "", "", "", "", "");
    }

    @Test
    void error_404_when_changing_contact_information_of_an_unknown_member() throws Exception {
        String unknownMember = "memberId";
        doThrow(new NoSuchElementException())
                .when(service)
                .changeMemberContactInformation(unknownMember, "", "", "", "",
                        LocalDate.of(1970, 1, 1), "", "");

        String request = "{" +
                "\"lastName\":\"\", " +
                "\"firstName\":\"\"," +
                "\"email\":\"\"," +
                "\"gender\":\"\"," +
                "\"phone\":\"\"," +
                "\"mobile\":\"\"," +
                "\"birthDate\":\"01/01/1970\"" +
                "}";
        mockMvc.perform(post("/members/" + unknownMember + "/changeContactInformation")
                .header(AUTHORIZATION, BASE64_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
                .characterEncoding(StandardCharsets.UTF_8.name()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("No member found with id: memberId"));

        verify(service).changeMemberContactInformation(unknownMember, "", "", "", "",
                LocalDate.of(1970, 1, 1), "", "");
    }

    @Test
    void only_authenticated_user_can_access_members_api() throws Exception {
        mockMvc.perform(get(MEMBERS))
                .andExpect(status().isForbidden());
    }

    @Test
    void fetch_members_with_default_pagination_and_sort() throws Exception {
        when(service.findMembers(1, 20, "lastName", "ASC")).thenReturn(new Page<>(2, List.of(
                new Member("1", "LASTNAMEA", "FirstNameA", "abc@def.com",
                        LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                        new PhoneNumber("0606060606"), "user", Instant.now()),
                new Member("2", "LASTNAMEB", "FirstNameB", "abc@def.com",
                        LocalDate.of(1970, 1, 1), Gender.MALE, new PhoneNumber("0401020304"),
                        new PhoneNumber("0606060606"), "user", Instant.now())
        )));

        mockMvc.perform(get(MEMBERS)
                .header(AUTHORIZATION, BASE64_TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(2))
                .andExpect(jsonPath("$.members", hasSize(2)))
                .andExpect(jsonPath("$.members[0].id").value("1"))
                .andExpect(jsonPath("$.members[1].id").value("2"));

        verify(service).findMembers(1, 20, "lastName", "ASC");
    }

    @Test
    void import_a_member() throws Exception {
        String data = "{\"licenceNumber\":\"PC11737992\",\"firstName\":\"john\",\"lastName\":\"doe\",\"gender\":\"M\",\"birthDate\":\"1967-01-01\",\"email\":\"john.doe@mail.fr\",\"phone\":\"\",\"mobile\":\"\",\"line1\":\"1 chemin de la localisation\",\"line2\":\"\",\"line3\":\"Lieu-Dit\",\"zipCode\":\"12345\",\"city\":\"Ma Ville\"}";
        String encodedData = Base64.getEncoder().encodeToString(data.getBytes());
        Body body = new Body();
        body.setMessage(new Body.Message("1", "", encodedData));
        String content = new ObjectMapper().writeValueAsString(body);
        mockMvc.perform(post("/members/import")
                .header(AUTHORIZATION, BASE64_TOKEN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content)
                .characterEncoding("UTF-8"))
                .andExpect(status().isNoContent());
    }
}
