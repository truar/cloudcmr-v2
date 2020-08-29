package com.cloud.cmr.exposition.member;

import java.time.Instant;

public class MemberDTO {
    public String lastName;
    public String firstName;
    public String email;
    public Instant createdAt;
    public String creator;

    public MemberDTO() {
    }

    public MemberDTO(String lastName, String firstName, String email, Instant createdAt, String creator) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.createdAt = createdAt;
        this.creator = creator;
    }
}
