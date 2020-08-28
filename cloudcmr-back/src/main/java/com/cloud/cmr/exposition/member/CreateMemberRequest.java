package com.cloud.cmr.exposition.member;

public class CreateMemberRequest {

    public final String lastName;
    public final String firstName;
    public final String email;

    public CreateMemberRequest(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }
}
