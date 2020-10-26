package com.cloud.cmr.cloudcmrfunctions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class ImportMembersFunction {

    public static void main(String[] args) {
        SpringApplication.run(ImportMembersFunction.class, args);
    }

    private final ImportMembersService importMembersService;

    public ImportMembersFunction(ImportMembersService importMembersService) {
        this.importMembersService = importMembersService;
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
