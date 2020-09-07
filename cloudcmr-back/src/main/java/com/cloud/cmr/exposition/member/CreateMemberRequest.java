package com.cloud.cmr.exposition.member;

public class CreateMemberRequest {

    public final String lastName;
    public final String firstName;
    public final String email;
    public final String mobile;
    public final String gender;
    public final String phone;
    public final String birthDate;

    public CreateMemberRequest(String lastName, String firstName, String email, String mobile, String gender, String phone, String birthDate) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.phone = phone;
        this.birthDate = birthDate;
    }
}
