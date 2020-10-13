package com.cloud.cmr.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Unindexed;
import org.springframework.data.annotation.Id;

import java.text.Normalizer;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Entity(name = "members")
public class Member {

    private static final Pattern PATTERN = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$");

    @Id
    private String id;
    private String lastName;
    private String firstName;
    private String email;
    private PhoneNumber phone;
    private PhoneNumber mobile;
    @Unindexed
    private LocalDate birthDate;
    @Unindexed
    private Gender gender;
    @Unindexed
    private String creator;
    @Unindexed
    private Instant createdAt;
    @Unindexed
    private Address address;

    public Member(String id, String lastName, String firstName, String email, LocalDate birthDate, Gender gender, PhoneNumber phone, PhoneNumber mobile, String creator, Instant createdAt) {
        this.id = id;
        setLastName(lastName);
        setFirstName(firstName);
        this.email = email;
        this.birthDate = birthDate;
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
        if (StringUtils.isBlank(lastName)) {
            throw new IllegalArgumentException("LastName must not be blank");
        }

        if (!PATTERN.matcher(lastName).matches()) {
            throw new IllegalArgumentException("The lastname \"" + lastName + "\" contains illegal character");
        }

        this.lastName = Normalizer.normalize(lastName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toUpperCase();
    }

    private void setFirstName(String firstName) {
        if (StringUtils.isBlank(firstName)) {
            throw new IllegalArgumentException("Firstname must not be blank");
        }

        if (!PATTERN.matcher(firstName).matches()) {
            throw new IllegalArgumentException("The firstName \"" + firstName + "\" contains illegal character");
        }

        String tmpFirstName = Normalizer.normalize(firstName, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        tmpFirstName = Arrays.stream(tmpFirstName.split("[ ]"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
        this.firstName = Arrays.stream(tmpFirstName.split("[-]"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining("-"));
    }

    public void changeAddress(Address address) {
        this.address = address;
    }
}
