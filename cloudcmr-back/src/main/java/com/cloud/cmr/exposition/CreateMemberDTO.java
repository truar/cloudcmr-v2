package com.cloud.cmr.exposition;

public class CreateMemberDTO {

    public final String lastName;
    public final String firstName;
    public final String email;

    public CreateMemberDTO(String lastName, String firstName, String email) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
    }
}
