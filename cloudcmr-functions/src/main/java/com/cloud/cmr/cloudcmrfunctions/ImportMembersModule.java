package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.function.Consumer;

@SpringBootApplication
public class ImportMembersModule {

    public static void main(String[] args) {
        SpringApplication.run(ImportMembersModule.class, args);
    }

    @Bean
    public Consumer<GscEvent> gcsEvent() {
        return message -> {
            System.out.println(message.getBucket());
            System.out.println(message.getName());
            System.out.println(message.getUpdated());
            System.out.println(message.getTimeCreated());
            System.out.println(message.getMetageneration());
        };
    }

    private class GscEvent {
        // Cloud Functions uses GSON to populate this object.
        // Field types/names are specified by Cloud Functions
        // Changing them may break your code!
        private String bucket;
        private String name;
        private String metageneration;
        private Date timeCreated;
        private Date updated;

        public String getBucket() {
            return bucket;
        }

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getMetageneration() {
            return metageneration;
        }

        public void setMetageneration(String metageneration) {
            this.metageneration = metageneration;
        }

        public Date getTimeCreated() {
            return timeCreated;
        }

        public void setTimeCreated(Date timeCreated) {
            this.timeCreated = timeCreated;
        }

        public Date getUpdated() {
            return updated;
        }

        public void setUpdated(Date updated) {
            this.updated = updated;
        }
    }
}
