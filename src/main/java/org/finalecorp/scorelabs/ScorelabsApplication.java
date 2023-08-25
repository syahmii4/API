package org.finalecorp.scorelabs;

import org.finalecorp.scorelabs.models.Users;
import org.finalecorp.scorelabs.services.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class ScorelabsApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScorelabsApplication.class, args);
	}


}
