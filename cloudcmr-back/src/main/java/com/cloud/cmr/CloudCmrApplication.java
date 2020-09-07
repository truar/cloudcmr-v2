package com.cloud.cmr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gcp.data.datastore.repository.config.EnableDatastoreAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableDatastoreAuditing
public class CloudCmrApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudCmrApplication.class, args);
	}
}
