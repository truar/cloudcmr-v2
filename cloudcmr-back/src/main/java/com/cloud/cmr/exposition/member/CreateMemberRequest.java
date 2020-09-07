package com.cloud.cmr.exposition.member;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class CreateMemberRequest {

    public final String lastName;
    public final String firstName;
    public final String email;
    public final String mobile;
    public final String gender;
    public final String phone;
    @JsonFormat(pattern = "dd/MM/yyyy")
    public final LocalDate birthDate;

    public CreateMemberRequest(String lastName, String firstName, String email, String mobile, String gender, String phone, LocalDate birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
