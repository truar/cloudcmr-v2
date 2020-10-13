package com.cloud.cmr.domain.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberDomainTest {

    @Nested
    @DisplayName("Test the last name normalization")
    class LastNameTest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void last_name_should_be_not_null_nor_empty_nor_blank(String badLastName) {
            assertThrows(IllegalArgumentException.class, () -> createMemberWithLastName(badLastName));
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "%", "&", "#", "<", ">", "/", "?"})
        void last_name_should_not_contains_unexpected_character(String badInput) {
            assertThrows(IllegalArgumentException.class, () -> createMemberWithLastName(badInput));
        }

        @Test
        void last_name_is_always_uppercased() {
            Member member = createMemberWithLastName("lastname");
            assertThat(member.getLastName()).isEqualTo("LASTNAME");
        }

        @Test
        void accents_are_transformed_to_regular_char() {
            Member member = createMemberWithLastName("èéêëàâ");
            assertThat(member.getLastName()).isEqualTo("EEEEAA");
        }

        @Test
        void spaces_are_stored_the_same_way() {
            Member member = createMemberWithLastName("last name");
            assertThat(member.getLastName()).isEqualTo("LAST NAME");
        }

        @Test
        void dashes_are_stored_the_same_way() {
            Member member = createMemberWithLastName("last-name");
            assertThat(member.getLastName()).isEqualTo("LAST-NAME");
        }

        private Member createMemberWithLastName(String lastName) {
            return new Member("id", lastName, "Firstname", "abc@drf.com", LocalDate.parse("2020-01-01"),
                    Gender.MALE, new PhoneNumber("0102030405"), new PhoneNumber("0102030405"), "user", Instant.EPOCH);
        }
    }

    @Nested
    @DisplayName("Test the first name normalization")
    class FirstNameTest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" "})
        void first_name_should_be_not_null_nor_empty_nor_blank(String badFirstName) {
            assertThrows(IllegalArgumentException.class, () -> createMemberWithFirstName(badFirstName));
        }

        @ParameterizedTest
        @ValueSource(strings = {"@", "%", "&", "#", "<", ">", "/", "?"})
        void first_name_should_not_contains_unexpected_character(String badFirstName) {
            assertThrows(IllegalArgumentException.class, () -> createMemberWithFirstName(badFirstName));
        }

        @Test
        void first_name_is_always_capitalized() {
            Member member = createMemberWithFirstName("firstname");
            assertThat(member.getFirstName()).isEqualTo("Firstname");
        }

        @Test
        void accents_are_transformed_to_regular_char() {
            Member member = createMemberWithFirstName("èéêëàâ");
            assertThat(member.getFirstName()).isEqualTo("Eeeeaa");
        }

        @Test
        void each_word_separated_by_spaces_are_capitalized() {
            Member member = createMemberWithFirstName("first name");
            assertThat(member.getFirstName()).isEqualTo("First Name");
        }

        @Test
        void each_word_separated_by_dashes_are_capitalized() {
            Member member = createMemberWithFirstName("first-name");
            assertThat(member.getFirstName()).isEqualTo("First-Name");
        }

        private Member createMemberWithFirstName(String firstName) {
            return new Member("id", "LASTNAME", firstName, "abc@drf.com", LocalDate.parse("2020-01-01"),
                    Gender.MALE, new PhoneNumber("0102030405"), new PhoneNumber("0102030405"), "user", Instant.EPOCH);
        }
    }
}
