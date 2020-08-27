package com.cloud.cmr.domain.member;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Member {

    @Id
    private String id;

    private String lastName;
    private String firstName;
    private String email;
    private String creator;
    private Instant createdAt;

    protected Member() {
        // For hibernate
    }

    public Member(String id, String lastName, String firstName, String email, String creator, Instant createdAt) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
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

    public String getCreator() {
        return creator;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
