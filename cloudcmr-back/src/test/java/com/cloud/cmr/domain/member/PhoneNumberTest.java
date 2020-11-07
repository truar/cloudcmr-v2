package com.cloud.cmr.domain.member;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {

    @Test
    void a_phone_number_does_contains_spaces_every_2_digits() {
        var number = new PhoneNumber("0601020304");
        assertThat(number.getNumber()).isEqualTo("06 01 02 03 04");
    }

    @Test
    void dots_are_converted_to_spaces() {
        var number = new PhoneNumber("06.01.02.03.04");
        assertThat(number.getNumber()).isEqualTo("06 01 02 03 04");
    }

    @Test
    void french_international_number_are_stored_as_classic_06() {
        var number = new PhoneNumber("+33601020304");
        assertThat(number.getNumber()).isEqualTo("06 01 02 03 04");

        number = new PhoneNumber("+33 0601020304");
        assertThat(number.getNumber()).isEqualTo("06 01 02 03 04");
    }

    @Test
    void international_number_are_stored_like_international() {
        var number = new PhoneNumber("+41221020304");
        assertThat(number.getNumber()).isEqualTo("+41 22 102 03 04");
    }

    @Test
    void phone_number_can_be_stored() {
        var number = new PhoneNumber("0450010203");
        assertThat(number.getNumber()).isEqualTo("04 50 01 02 03");
    }

    @Test
    void phone_number_can_be_empty() {
        var number = new PhoneNumber("");
        assertThat(number.getNumber()).isEqualTo("");

        number = new PhoneNumber(null);
        assertThat(number.getNumber()).isEqualTo(null);
    }

    @Test
    void can_not_store_an_illegal_phone_number() {
        assertThatThrownBy(() -> new PhoneNumber("abc"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The number \"abc\" is invalid. Format required = (+XX) XX.XX.XX.XX.XX");

        assertThatThrownBy(() -> new PhoneNumber("0102"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("The number \"0102\" is invalid. Format required = (+XX) XX.XX.XX.XX.XX");
    }
}