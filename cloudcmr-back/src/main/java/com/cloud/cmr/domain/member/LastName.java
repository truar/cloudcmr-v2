package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.ValueObject;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class LastName extends ValueObject {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$");

    private String value;

    public LastName(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("LastName must not be blank");
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("The lastname \"" + value + "\" contains illegal character");
        }

        this.value = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .toUpperCase();
    }

    public String getValue() {
        return value;
    }
}
