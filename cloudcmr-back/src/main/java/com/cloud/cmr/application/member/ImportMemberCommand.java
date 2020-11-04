package com.cloud.cmr.application.member;

import java.time.LocalDate;

public class ImportMemberCommand {

    private final String licenceNumber;
    private final String lastName;
    private final String firstName;
    private final String email;
    private final LocalDate birthDate;
    private final String gender;
    private final String phone;
    private final String mobile;
    private final String line1;
    private final String line2;
    private final String line3;
    private final String zipCode;
    private final String city;

    public ImportMemberCommand(String licenceNumber, String lastName, String firstName, String email, LocalDate birthDate, String gender, String phone, String mobile, String line1, String line2, String line3, String zipCode, String city) {
        this.licenceNumber = licenceNumber;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.mobile = mobile;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.zipCode = zipCode;
        this.city = city;
    }

    public String getLicenceNumber() {
        return licenceNumber;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getMobile() {
        return mobile;
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
}
