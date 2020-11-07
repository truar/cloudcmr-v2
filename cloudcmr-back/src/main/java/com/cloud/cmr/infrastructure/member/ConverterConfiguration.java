package com.cloud.cmr.infrastructure.member;

import com.cloud.cmr.domain.member.FirstName;
import com.cloud.cmr.domain.member.LastName;
import org.springframework.cloud.gcp.data.datastore.core.convert.DatastoreCustomConversions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.util.Arrays;

@Configuration
public class ConverterConfiguration {

    static final Converter<LastName, String> LASTNAME_STRING_CONVERTER =
            new Converter<LastName, String>() {
                @Override
                public String convert(LastName name) {
                    return name.getValue();
                }
            };

    static final Converter<String, LastName> STRING_LASTNAME_CONVERTER =
            new Converter<String, LastName>() {
                @Override
                public LastName convert(String s) {
                    return new LastName(s);
                }
            };

    static final Converter<FirstName, String> FIRSTNAME_STRING_CONVERTER =
            new Converter<FirstName, String>() {
                @Override
                public String convert(FirstName name) {
                    return name.getValue();
                }
            };

    static final Converter<String, FirstName> STRING_FIRSTNAME_CONVERTER =
            new Converter<String, FirstName>() {
                @Override
                public FirstName convert(String s) {
                    return new FirstName(s);
                }
            };

    @Bean
    public DatastoreCustomConversions datastoreCustomConversions() {
        return new DatastoreCustomConversions(
                Arrays.asList(
                        LASTNAME_STRING_CONVERTER,
                        STRING_LASTNAME_CONVERTER,
                        FIRSTNAME_STRING_CONVERTER,
                        STRING_FIRSTNAME_CONVERTER));
    }

}
