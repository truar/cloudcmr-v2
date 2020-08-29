package com.cloud.cmr.domain.member;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Entity(name = "members")
public class Member {

    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private PhoneNumber phone;
    private PhoneNumber mobile;
    @Unindexed
    private Gender gender;
    @Unindexed
    private String creator;
    @Unindexed
    private Instant createdAt;
    @Unindexed
    private Address address;

    public Member(String id, String lastName, String firstName, String email, Gender gender, PhoneNumber phone, PhoneNumber mobile, String creator, Instant createdAt) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.gender = gender;
        this.phone = phone;
        this.mobile = mobile;
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

    public Gender getGender() {
        return gender;
    }

    public String getPhone() {
        return phone.getNumber();
    }

    public String getMobile() {
        return mobile.getNumber();
    }

    public Address getAddress() {
        return address;
    }

    public void changeAddress(Address address) {
        this.address = address;
    }
}
