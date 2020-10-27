package com.cloud.cmr.exposition.member;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

import java.time.Instant;
import java.time.LocalDate;

public class MemberDTO {
    public final String lastName;
    public final String firstName;
    public final String email;
    public final String gender;
    public final String phone;
    public final String mobile;
    public final Instant createdAt;
    public final String creator;
    public final AddressDTO address;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    public final LocalDate birthDate;

    public MemberDTO(String lastName, String firstName, String email, String gender, LocalDate birthDate, String phone, String mobile, AddressDTO address, Instant createdAt, String creator) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.email = email;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phone = phone;
        this.mobile = mobile;
        this.address = address;
        this.createdAt = createdAt;
        this.creator = creator;
    }
}
