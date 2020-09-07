package com.cloud.cmr.domain.member;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Entity
public class LastName {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$");

    private final String lastName;

    public LastName(String lastName) {

        this.lastName = lastName;
    }

    public String getLastName() {
        return this.lastName;
    }
}
