package com.cloud.cmr.cloudcmrfunctions;

import com.google.common.primitives.UnsignedLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.function.Consumer;

@SpringBootApplication
public class ImportMembersFunction {

    @Autowired
    private ApplicationContext context;

    public static void main(String[] args) {
        SpringApplication.run(ImportMembersFunction.class, args);
    }

    @Bean
    public Consumer<GscEvent> gcsEvent() {
        return message -> {
            System.out.println(message.getBucket());
            System.out.println(message.getName());
            System.out.println(message.getUpdated());
            System.out.println(message.getTimeCreated());
            System.out.println(message.getMetageneration());
            System.out.println("size = " + message.getSize());
            System.out.println("mediaLink = " + message.getMediaLink());
            System.out.println("selfLink = " + message.getSelfLink());
            Resource resource = context.getResource(String.format("gs://%s/%s", message.getBucket(), message.getName()));
            try {
                String s = StreamUtils.copyToString(
                        resource.getInputStream(),
                        Charset.defaultCharset());
                System.out.println(s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }

    private class GscEvent {
        // Cloud Functions uses GSON to populate this object.
        // Field types/names are specified by Cloud Functions
        // Changing them may break your code!
        private String bucket;
        private String name;
        private String metageneration;
        private LocalDateTime timeCreated;
        private LocalDateTime updated;
        private String selfLink;
        private String mediaLink;
        private UnsignedLong size;

        public UnsignedLong getSize() {
            return size;
        }

        public void setSize(UnsignedLong size) {
            this.size = size;
        }

        public String getMediaLink() {
            return mediaLink;
        }

        public void setMediaLink(String mediaLink) {
            this.mediaLink = mediaLink;
        }

        public String getSelfLink() {
            return selfLink;
        }

        public void setSelfLink(String selfLink) {
            this.selfLink = selfLink;
        }

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

        public LocalDateTime getTimeCreated() {
            return timeCreated;
        }

        public void setTimeCreated(LocalDateTime timeCreated) {
            this.timeCreated = timeCreated;
        }

        public LocalDateTime getUpdated() {
            return updated;
        }

        public void setUpdated(LocalDateTime updated) {
            this.updated = updated;
        }
    }
}
