package com.ash.incubation.incubation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude={SecurityAutoConfiguration.class})
public class SpringSecurityByAshApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityByAshApplication.class, args);
	}

}
