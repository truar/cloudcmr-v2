package com.cloud.cmr.domain.member;

import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity
public class PhoneNumber {
    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
