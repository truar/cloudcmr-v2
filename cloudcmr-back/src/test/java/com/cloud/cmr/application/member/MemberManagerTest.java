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
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC));

        String memberId = service.create("lastName", "firstName", "abc.mail.com", "MALE",
                LocalDate.of(1970, 1, 1), "0102030405", "0601020304", "user");

        assertThat(memberId).isEqualTo("1");
        verify(memberRepository).save(refEq(new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"))));
    }

    @Test
    void change_the_address_of_a_member() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        Member member = new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"));
        when(memberRepository.findById("1")).thenReturn(member);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC));

        service.changeMemberAddress("1", "line1", "line2", "line3", "city", "zipCode");

        assertThat(member.getAddress()).isEqualTo(new Address("line1", "line2", "line3", "city", "zipCode"));
        verify(memberRepository).save(any());
    }

    @Test
    void change_the_contact_information_of_a_member() {
        MemberRepository memberRepository = mock(MemberRepository.class);
        Member member = new Member("1", "lastName", "firstName",
                "abc.mail.com", LocalDate.of(1970, 1, 1), Gender.MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "user", Instant.parse("2020-01-01T10:00:00Z"));
        when(memberRepository.findById("1")).thenReturn(member);
        MemberManager service = new MemberManager(memberRepository, Clock.fixed(Instant.parse("2020-01-01T10:00:00Z"), ZoneOffset.UTC));

        service.changeMemberContactInformation("1", "newLastName", "newFirstName", "abc.mail.com", "MALE", LocalDate.of(1970, 1, 1), "0102030405", "0601020304");

        assertThat(member.getLastName()).isEqualTo("NEWLASTNAME");
        assertThat(member.getFirstName()).isEqualTo("NewFirstName");
        verify(memberRepository).save(any());
    }
}