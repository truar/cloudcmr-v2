package com.cloud.cmr.cloudcmrfunctions;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static org.apache.commons.lang3.builder.ToStringStyle.JSON_STYLE;

public class AddressDTO {
    private final String line1;
    private final String line2;
    private final String line3;
    private final String zipCode;
    private final String city;

    public AddressDTO(String line1, String line2, String line3, String zipCode, String city) {
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.zipCode = zipCode;
        this.city = city;
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

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, JSON_STYLE);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
