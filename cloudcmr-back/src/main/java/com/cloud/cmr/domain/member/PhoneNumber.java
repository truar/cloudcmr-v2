package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.ValueObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity
public class PhoneNumber extends ValueObject {
    public static final String DEFAULT_REGION = "FR";
    private String number;

    public PhoneNumber(String number) {
        setNumber(number);
    }

    private void setNumber(String number) {
        if (number == null || number.isEmpty()) {
            this.number = number;
            return;
        }
        var phoneUtil = PhoneNumberUtil.getInstance();
        var numberProto = parsePhoneNumber(number, phoneUtil);
        if (!phoneUtil.isValidNumber(numberProto)) {
            throw createIllegalArgumentException(number);
        }
        var format = determineNumberFormat(number);
        this.number = phoneUtil.format(numberProto, format);
    }

    private Phonenumber.PhoneNumber parsePhoneNumber(String number, PhoneNumberUtil phoneUtil) {
        try {
            return phoneUtil.parse(number, DEFAULT_REGION);
        } catch (NumberParseException e) {
            throw createIllegalArgumentException(number);
        }
    }

    private IllegalArgumentException createIllegalArgumentException(String number) {
        return new IllegalArgumentException("The number \"" + number + "\" is invalid. Format required = (+XX) XX.XX.XX.XX.XX");
    }

    private PhoneNumberUtil.PhoneNumberFormat determineNumberFormat(String number) {
        PhoneNumberUtil.PhoneNumberFormat format;
        if (isAFrenchNumber(number)) {
            format = PhoneNumberUtil.PhoneNumberFormat.NATIONAL;
        } else {
            format = PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
        }
        return format;
    }

    private boolean isAFrenchNumber(String number) {
        return number.startsWith("0") || number.startsWith("+33");
    }

    public String getNumber() {
        return number;
    }
}
