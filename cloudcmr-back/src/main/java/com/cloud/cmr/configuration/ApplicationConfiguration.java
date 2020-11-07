package com.cloud.cmr.configuration;

import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Clock;
import java.time.ZoneId;

@Profile("!test")
@Configuration
@EnableTransactionManagement
@EnableDatastoreAuditing
public class ApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
    }
}
