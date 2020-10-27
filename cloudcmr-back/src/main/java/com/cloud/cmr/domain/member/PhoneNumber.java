package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.ValueObject;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

@Entity
public class PhoneNumber extends ValueObject {
    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
