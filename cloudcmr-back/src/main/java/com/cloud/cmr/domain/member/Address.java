package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.ValueObject;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity
public class Address extends ValueObject {
    private String line1;
    private String line2;
    private String line3;
    private String city;
    private String zipCode;

    public Address(String line1, String line2, String line3, String city, String zipCode) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.city = city;
        this.zipCode = zipCode;
    }

    public String getLine1() {
        return line1;
    }

    public String getLine2() {
        return line2;
    }

    public String getLine3() {
        return line3;
    }

    public String getCity() {
        return city;
    }

    public String getZipCode() {
        return zipCode;
    }
}
