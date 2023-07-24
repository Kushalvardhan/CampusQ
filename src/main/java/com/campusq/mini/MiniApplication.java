package com.campusq.mini;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
@SpringBootApplication
@EnableJpaRepositories("com.campusq.mini.repository")
@EntityScan("com.campusq.mini.model")
@EnableJpaAuditing(auditorAwareRef = "auditAwareClass")

public class MiniApplication {
	public static void main(String[] args) {
		SpringApplication.run(MiniApplication.class, args);
	}
}
