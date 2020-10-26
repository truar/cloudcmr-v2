package com.cloud.cmr.configuration.security.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "firebase")
public class FirebaseConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(FirebaseConfiguration.class);

    private String projectId;

    @PostConstruct
    public void init() throws IOException {
        try {
            // This happens only when hot-restarting the application, with Spring-devtool
            FirebaseApp.getInstance();
            logger.debug("Using Firebase already configured instance");
        } catch (IllegalStateException e) {
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setProjectId(projectId)
                    .setCredentials(GoogleCredentials.getApplicationDefault())
                    .build();

            FirebaseApp.initializeApp(options);
            logger.debug("Creating new Firebase instance");
        }

    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
}
