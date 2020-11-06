package com.cloud.cmr.application.member;

import com.cloud.cmr.domain.member.*;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class MemberManagerTest {

    @Test
    void create_and_save_a_member() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        when(memberRepository.nextId()).thenReturn("1");
        MemberImporter memberImporter = mock(MemberImporter.class);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC), memberImporter);

        String memberId = service.createMember("lastName", "firstName", "abc.mail.com", "MALE",
                LocalDate.of(1970, 1, 1), "0102030405", "0601020304", "user");

        assertThat(memberId).isEqualTo("1");
        verify(memberRepository).save(refEq(new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"))));
    }

    @Test
    void import_a_member_from_an_external_source() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        when(memberRepository.nextId()).thenReturn("1");
        MemberImporter memberImporter = mock(MemberImporter.class);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC), memberImporter);

        ImportMemberCommand importMemberCommand = new ImportMemberCommand("licence", "lastName", "firstName", "abc@mail.com",
                LocalDate.of(1970, 1, 1), "MALE", "0102030405", "0601020304",
                "line1", "line2", "line3", "zipCode", "city");
        service.importMember(importMemberCommand);

        verify(memberRepository).save(any());
        verify(memberImporter).importMemberFromExternalSource("licence", "lastName", "firstName", "abc@mail.com",
                LocalDate.of(1970, 1, 1), "MALE", "0102030405", "0601020304",
                "line1", "line2", "line3", "zipCode", "city");
    }

    @Test
    void change_the_address_of_a_member() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        MemberImporter memberImporter = mock(MemberImporter.class);
        Member member = new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"));
        when(memberRepository.findById("1")).thenReturn(member);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC), memberImporter);

        service.changeMemberAddress("1", "line1", "line2", "line3", "city", "zipCode");

        assertThat(member.getAddress()).isEqualTo(new Address("line1", "line2", "line3", "city", "zipCode"));
        verify(memberRepository).save(any());
    }

    @Test
    void change_the_contact_information_of_a_member() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        MemberImporter memberImporter = mock(MemberImporter.class);
        Member member = new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"));
        when(memberRepository.findById("1")).thenReturn(member);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC), memberImporter);

        service.changeMemberContactInformation("1", "newLastName", "newFirstName", "abc.mail.com", "MALE", LocalDate.of(1970, 1, 1), "0102030405", "0601020304");

        assertThat(member.getLastName()).isEqualTo("NEWLASTNAME");
        assertThat(member.getFirstName()).isEqualTo("Newfirstname");
        verify(memberRepository).save(any());
    }
}