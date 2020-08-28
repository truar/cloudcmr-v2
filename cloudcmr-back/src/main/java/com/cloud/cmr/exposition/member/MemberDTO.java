package com.cloud.cmr.exposition.member;

import java.time.Instant;

public class MemberDTO {
    public final String lastName;
    public final String firstName;
    public final String email;
    public final Instant createdAt;
    public final String creator;

    public MemberDTO(String lastName, String firstName, String email, Instant createdAt, String creator) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.createdAt = createdAt;
        this.creator = creator;
    }
}
