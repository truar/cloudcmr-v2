package com.cloud.cmr.exposition.member;

import java.time.Instant;

public class MemberDTO {
    public String lastName;
    public String firstName;
    public String email;
    public String gender;
    public String phone;
    public String mobile;
    public Instant createdAt;
    public String creator;

    public MemberDTO() {
    }

    public MemberDTO(String lastName, String firstName, String email, String gender, String phone, String mobile, Instant createdAt, String creator) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.mobile = mobile;
        this.createdAt = createdAt;
        this.creator = creator;
    }
}
