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
        return message -> importMembersService.importMemberFromGcpStorage(
                message.getBucket(),
                message.getName());
    }
}
