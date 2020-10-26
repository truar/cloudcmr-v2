package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.time.Clock;
import java.time.ZoneId;
import java.util.function.Consumer;

@SpringBootApplication
@ComponentScan({"com.cloud.cmr.cloudcmrfunctions",
        "com.cloud.cmr.domain",
        "com.cloud.cmr.infrastructure.member"})
public class ImportMembersFunction {

    public static void main(String[] args) {
        SpringApplication.run(ImportMembersFunction.class, args);
    }

    @Autowired
    private ImportMembersService importMembersService;

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Paris"));
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
            importMembersService.importMemberFromGcpStorage(message.getBucket(), message.getName());
        };
    }
}
