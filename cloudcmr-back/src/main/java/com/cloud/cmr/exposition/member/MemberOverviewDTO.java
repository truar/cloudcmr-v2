package com.cloud.cmr.exposition.member;

public class MemberOverviewDTO {
    public final String id;
    public final String lastName;
    public final String firstName;
    public final String email;

    public MemberOverviewDTO(String id, String lastName, String firstName, String email) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }
}
