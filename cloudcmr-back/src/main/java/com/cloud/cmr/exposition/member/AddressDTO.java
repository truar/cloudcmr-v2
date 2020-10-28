package com.cloud.cmr.exposition.member;

public class AddressDTO {
    public final String line1;
    public final String line2;
    public final String line3;
    public final String city;
    public final String zipCode;

    public AddressDTO(String line1, String line2, String line3, String city, String zipCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.city = city;
        this.zipCode = zipCode;
    }
}
