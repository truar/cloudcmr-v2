package com.cloud.cmr.domain.member;

import com.cloud.cmr.domain.common.ValueObject;
import org.apache.commons.lang3.StringUtils;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FirstName extends ValueObject {
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-ZàáâäãåąčćęèéêëėįìíîïłńòóôöõøùúûüųūÿýżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËÌÍÎÏĮŁŃÒÓÔÖÕØÙÚÛÜŲŪŸÝŻŹÑßÇŒÆČŠŽ∂ð ,.'-]+$");

    private final String value;

    public FirstName(String value) {
        if (StringUtils.isBlank(value)) {
            throw new IllegalArgumentException("Firstname must not be blank");
        }

        if (!PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("The firstname \"" + value + "\" contains illegal character");
        }

        String tmpFirstName = Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
        tmpFirstName = Arrays.stream(tmpFirstName.split("[ ]"))
                .map(String::toLowerCase)
                .map(StringUtils::capitalize)
                .collect(Collectors.joining(" "));
        this.value = Arrays.stream(tmpFirstName.split("[-]"))
                .map(StringUtils::capitalize)
                .collect(Collectors.joining("-"));
    }

    public String getValue() {
        return value;
    }
}
