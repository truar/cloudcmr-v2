package com.cloud.cmr.domain.member;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;

import static com.cloud.cmr.domain.member.Gender.MALE;
import static org.mockito.Mockito.*;

class MemberImporterTest {

    @Test
    void create_a_new_member_if_the_member_does_not_exist_yet_based_on_the_external_data() {
        Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(Instant.parse("2020-11-04T12:00:00Z"));
        String licence = "licence";
        MemberRepository memberRepository = mock(MemberRepository.class);
        when(memberRepository.nextId()).thenReturn("1");
        when(memberRepository.findByLicenceNumber(licence)).thenReturn(Optional.empty());
        MemberImporter memberImporter = new MemberImporter(memberRepository, clock);

        Member member = memberImporter.importMemberFromExternalSource(licence, "lastName", "firstName", "abc@mail.com",
                LocalDate.of(1970, 1, 1), "MALE", "0102030405", "0601020304",
                "line1", "line2", "line3", "zipCode", "city");

        SoftAssertions memberAssertions = new SoftAssertions();
        memberAssertions.assertThat(member.getId()).isEqualTo("1");
        memberAssertions.assertThat(member.getLastName()).isEqualTo("LASTNAME");
        memberAssertions.assertThat(member.getFirstName()).isEqualTo("FirstName");
        memberAssertions.assertThat(member.getEmail()).isEqualTo("abc@mail.com");
        memberAssertions.assertThat(member.getBirthDate()).isEqualTo(LocalDate.of(1970, 1, 1));
        memberAssertions.assertThat(member.getGender()).isEqualTo(MALE);
        memberAssertions.assertThat(member.getPhone()).isEqualTo("0102030405");
        memberAssertions.assertThat(member.getMobile()).isEqualTo("0601020304");
        memberAssertions.assertThat(member.getLicenceNumber()).isEqualTo(licence);
        memberAssertions.assertThat(member.getCreatedAt()).isEqualTo(Instant.parse("2020-11-04T12:00:00Z"));
        memberAssertions.assertThat(member.getCreator()).isEqualTo("importMemberService");
        Address address = member.getAddress();
        memberAssertions.assertThat(address.getLine1()).isEqualTo("line1");
        memberAssertions.assertThat(address.getLine2()).isEqualTo("line2");
        memberAssertions.assertThat(address.getLine3()).isEqualTo("line3");
        memberAssertions.assertThat(address.getZipCode()).isEqualTo("zipCode");
        memberAssertions.assertThat(address.getCity()).isEqualTo("city");
        memberAssertions.assertAll();

        SoftAssertions memberExternalDataAssertions = new SoftAssertions();
        MemberExternalData data = member.getExternalData();
        memberExternalDataAssertions.assertThat(data.getLicenceNumber()).isEqualTo(licence);
        memberExternalDataAssertions.assertThat(data.getLastName()).isEqualTo("lastName");
        memberExternalDataAssertions.assertThat(data.getFirstName()).isEqualTo("firstName");
        memberExternalDataAssertions.assertThat(data.getEmail()).isEqualTo("abc@mail.com");
        memberExternalDataAssertions.assertThat(data.getBirthDate()).isEqualTo(LocalDate.of(1970, 1, 1));
        memberExternalDataAssertions.assertThat(data.getGender()).isEqualTo("MALE");
        memberExternalDataAssertions.assertThat(data.getPhone()).isEqualTo("0102030405");
        memberExternalDataAssertions.assertThat(data.getMobile()).isEqualTo("0601020304");
        memberExternalDataAssertions.assertThat(data.getLine1()).isEqualTo("line1");
        memberExternalDataAssertions.assertThat(data.getLine2()).isEqualTo("line2");
        memberExternalDataAssertions.assertThat(data.getLine3()).isEqualTo("line3");
        memberExternalDataAssertions.assertThat(data.getZipCode()).isEqualTo("zipCode");
        memberExternalDataAssertions.assertThat(data.getCity()).isEqualTo("city");
        memberExternalDataAssertions.assertAll();

        verify(memberRepository).nextId();
    }

    @Test
    void update_only_external_data_if_a_member_is_already_in_the_system() {
        Clock clock = mock(Clock.class);
        String licence = "licence";
        MemberRepository memberRepository = mock(MemberRepository.class);
        Member mockedMember = new Member("1", "lastName", "firstName", "abc@mail.com",
                LocalDate.of(1970, 1, 1), MALE, new PhoneNumber("0102030405"),
                new PhoneNumber("0601020304"), "importMemberService", Instant.parse("2020-01-01T10:00:00Z"));
        mockedMember.changeAddress("line1", "line2", "line3", "city", "zipCode");
        when(memberRepository.findByLicenceNumber(licence)).thenReturn(Optional.of(mockedMember));

        MemberImporter memberImporter = new MemberImporter(memberRepository, clock);

        Member member = memberImporter.importMemberFromExternalSource(licence, "aName", "aFirstName", "anEmail@mail.com",
                LocalDate.of(1970, 1, 1), "MALE", "0203040506", "0606060606",
                "address1", "", "", "12345", "city");

        SoftAssertions memberAssertions = new SoftAssertions();
        memberAssertions.assertThat(member.getId()).isEqualTo("1");
        memberAssertions.assertThat(member.getLastName()).isEqualTo("LASTNAME");
        memberAssertions.assertThat(member.getFirstName()).isEqualTo("FirstName");
        memberAssertions.assertThat(member.getEmail()).isEqualTo("abc@mail.com");
        memberAssertions.assertThat(member.getBirthDate()).isEqualTo(LocalDate.of(1970, 1, 1));
        memberAssertions.assertThat(member.getGender()).isEqualTo(MALE);
        memberAssertions.assertThat(member.getPhone()).isEqualTo("0102030405");
        memberAssertions.assertThat(member.getMobile()).isEqualTo("0601020304");
        memberAssertions.assertThat(member.getCreatedAt()).isEqualTo(Instant.parse("2020-01-01T10:00:00Z"));
        memberAssertions.assertThat(member.getCreator()).isEqualTo("importMemberService");
        Address address = member.getAddress();
        memberAssertions.assertThat(address.getLine1()).isEqualTo("line1");
        memberAssertions.assertThat(address.getLine2()).isEqualTo("line2");
        memberAssertions.assertThat(address.getLine3()).isEqualTo("line3");
        memberAssertions.assertThat(address.getZipCode()).isEqualTo("zipCode");
        memberAssertions.assertThat(address.getCity()).isEqualTo("city");
        memberAssertions.assertAll();

        SoftAssertions memberExternalDataAssertions = new SoftAssertions();
        MemberExternalData data = member.getExternalData();
        memberExternalDataAssertions.assertThat(data.getLicenceNumber()).isEqualTo(licence);
        memberExternalDataAssertions.assertThat(data.getLastName()).isEqualTo("aName");
        memberExternalDataAssertions.assertThat(data.getFirstName()).isEqualTo("aFirstName");
        memberExternalDataAssertions.assertThat(data.getEmail()).isEqualTo("anEmail@mail.com");
        memberExternalDataAssertions.assertThat(data.getBirthDate()).isEqualTo(LocalDate.of(1970, 1, 1));
        memberExternalDataAssertions.assertThat(data.getGender()).isEqualTo("MALE");
        memberExternalDataAssertions.assertThat(data.getPhone()).isEqualTo("0203040506");
        memberExternalDataAssertions.assertThat(data.getMobile()).isEqualTo("0606060606");
        memberExternalDataAssertions.assertThat(data.getLine1()).isEqualTo("address1");
        memberExternalDataAssertions.assertThat(data.getLine2()).isEqualTo("");
        memberExternalDataAssertions.assertThat(data.getLine3()).isEqualTo("");
        memberExternalDataAssertions.assertThat(data.getZipCode()).isEqualTo("12345");
        memberExternalDataAssertions.assertThat(data.getCity()).isEqualTo("city");
        memberExternalDataAssertions.assertAll();
    }

    @Test
    void update_only_external_data_if_a_member_is_already_in_the_system_with_lastname_firstname_birthdate() {
        Clock clock = mock(Clock.class);
        String licence = "licence";
        MemberRepository memberRepository = mock(MemberRepository.class);
        String lastName = "lastName";
        String firstName = "firstName";
        LocalDate birthDate = LocalDate.of(1970, 1, 1);
        Member mockedMember = new Member("1", lastName, firstName, "abc@mail.com", birthDate, MALE,
                new PhoneNumber("0102030405"), new PhoneNumber("0601020304"), "importMemberService",
                Instant.parse("2020-01-01T10:00:00Z"));
        mockedMember.changeAddress("line1", "line2", "line3", "city", "zipCode");
        when(memberRepository.findByLicenceNumber(licence)).thenReturn(Optional.empty());
        when(memberRepository.findByLastNameAndFirstNameAndBirthDate(lastName, firstName, birthDate)).thenReturn(Optional.of(mockedMember));

        MemberImporter memberImporter = new MemberImporter(memberRepository, clock);

        Member member = memberImporter.importMemberFromExternalSource(licence, lastName, firstName, "anEmail@mail.com",
                birthDate, "MALE", "0203040506", "0606060606",
                "address1", "", "", "12345", "city");

        SoftAssertions memberAssertions = new SoftAssertions();
        memberAssertions.assertThat(member.getId()).isEqualTo("1");
        memberAssertions.assertThat(member.getLastName()).isEqualTo("LASTNAME");
        memberAssertions.assertThat(member.getFirstName()).isEqualTo("FirstName");
        memberAssertions.assertThat(member.getEmail()).isEqualTo("abc@mail.com");
        memberAssertions.assertThat(member.getBirthDate()).isEqualTo(LocalDate.of(1970, 1, 1));
        memberAssertions.assertThat(member.getGender()).isEqualTo(MALE);
        memberAssertions.assertThat(member.getPhone()).isEqualTo("0102030405");
        memberAssertions.assertThat(member.getMobile()).isEqualTo("0601020304");
        memberAssertions.assertThat(member.getCreatedAt()).isEqualTo(Instant.parse("2020-01-01T10:00:00Z"));
        memberAssertions.assertThat(member.getCreator()).isEqualTo("importMemberService");
        Address address = member.getAddress();
        memberAssertions.assertThat(address.getLine1()).isEqualTo("line1");
        memberAssertions.assertThat(address.getLine2()).isEqualTo("line2");
        memberAssertions.assertThat(address.getLine3()).isEqualTo("line3");
        memberAssertions.assertThat(address.getZipCode()).isEqualTo("zipCode");
        memberAssertions.assertThat(address.getCity()).isEqualTo("city");
        memberAssertions.assertAll();

        SoftAssertions memberExternalDataAssertions = new SoftAssertions();
        MemberExternalData data = member.getExternalData();
        memberExternalDataAssertions.assertThat(data.getLicenceNumber()).isEqualTo(licence);
        memberExternalDataAssertions.assertThat(data.getLastName()).isEqualTo(lastName);
        memberExternalDataAssertions.assertThat(data.getFirstName()).isEqualTo(firstName);
        memberExternalDataAssertions.assertThat(data.getEmail()).isEqualTo("anEmail@mail.com");
        memberExternalDataAssertions.assertThat(data.getBirthDate()).isEqualTo(birthDate);
        memberExternalDataAssertions.assertThat(data.getGender()).isEqualTo("MALE");
        memberExternalDataAssertions.assertThat(data.getPhone()).isEqualTo("0203040506");
        memberExternalDataAssertions.assertThat(data.getMobile()).isEqualTo("0606060606");
        memberExternalDataAssertions.assertThat(data.getLine1()).isEqualTo("address1");
        memberExternalDataAssertions.assertThat(data.getLine2()).isEqualTo("");
        memberExternalDataAssertions.assertThat(data.getLine3()).isEqualTo("");
        memberExternalDataAssertions.assertThat(data.getZipCode()).isEqualTo("12345");
        memberExternalDataAssertions.assertThat(data.getCity()).isEqualTo("city");
        memberExternalDataAssertions.assertAll();
    }
}