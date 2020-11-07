package com.cloud.cmr.cloudcmrfunctions;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.LocalDate;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

public class MemberAddressDTO {
    private final String licenceNumber;
    private final String firstName;
    private final String lastName;
    private final String gender;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private final LocalDate birthDate;
    private final String email;
    private final String phone;
    private final String mobile;
    private final String line1;
    private final String line2;
    private final String line3;
    private final String zipCode;
    private final String city;

    public MemberAddressDTO(String licenceNumber, String firstName, String lastName, String gender, LocalDate birthDate, String email, String phone, String mobile, String line1, String line2, String line3, String zipCode, String city) {
        this.licenceNumber = licenceNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
