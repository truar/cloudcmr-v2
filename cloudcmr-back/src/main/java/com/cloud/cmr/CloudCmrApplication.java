package com.cloud.cmr;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreAuditing;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Arrays;

@SpringBootApplication
@EnableTransactionManagement
@EnableDatastoreAuditing
public class CloudCmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudCmrApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner() {
		return args -> {
			if(args.length >= 1 && args[0].equals("--kill")) {
				System.exit(0);
			}
		};
	}
}
