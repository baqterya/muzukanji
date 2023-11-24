package com.baqterya.muzukanji;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

// TODO REMOVE EXCLUDE AFTER SECURITY IS SET UP
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class MuzukanjiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MuzukanjiApplication.class, args);
	}

}
