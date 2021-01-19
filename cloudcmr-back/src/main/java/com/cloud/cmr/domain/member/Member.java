package com.cloud.cmr.domain.member;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDate;

@Entity(name = "members")
public class Member {

    @Id
    private String id;
    private LastName lastName;
    private FirstName firstName;
    private LocalDate birthDate;
    @Unindexed
    private String email;
    @Unindexed
    private PhoneNumber phone;
    @Unindexed
    private PhoneNumber mobile;
    @Unindexed
    private String licenceNumber;
    @Unindexed
    private Gender gender;
    @Unindexed
    private String creator;
    @Unindexed
    private Instant createdAt;
    @Unindexed
    private Address address;
    @Unindexed
    private MemberExternalData externalData;

    public Member(String id, String lastName, String firstName, String email, LocalDate birthDate, Gender gender, PhoneNumber phone, PhoneNumber mobile, String creator, Instant createdAt) {
        this.id = id;
        setLastName(lastName);
        setFirstName(firstName);
        setEmail(email);
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.mobile = mobile;
        this.creator = creator;
        this.createdAt = createdAt;
    }

    protected Member() {
        // For Datastore
    }

    public String getId() {
        return id;
    }

    public String getLastName() {
        return lastName.getValue();
    }

    public String getFirstName() {
        return firstName.getValue();
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

    public LocalDate getBirthDate() {
        return birthDate;
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

    private void setLastName(String lastName) {
        this.lastName = new LastName(lastName);
    }

    private void setFirstName(String firstName) {
        this.firstName = new FirstName(firstName);
    }

    private void setEmail(String email) {
        this.email = email.toLowerCase();
    }

    public void changeContactInformation(String lastName, String firstName, String email, LocalDate birthDate, Gender gender, PhoneNumber phone, PhoneNumber mobile) {
        setLastName(lastName);
        setFirstName(firstName);
        setEmail(email);
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.mobile = mobile;
    }

    public void changeAddress(String line1, String line2, String line3, String city, String zipCode) {
        this.address = new Address(line1, line2, line3, city, zipCode);
    }

    public MemberExternalData getExternalData() {
        return this.externalData;
    }

    public void updateExternalData(MemberExternalData memberExternalData) {
        this.externalData = memberExternalData;
        this.licenceNumber = memberExternalData.getLicenceNumber();
    }

    public String getLicenceNumber() {
        return this.licenceNumber;
    }
}
