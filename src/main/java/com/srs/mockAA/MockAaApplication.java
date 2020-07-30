package com.srs.mockAA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class MockAaApplication {

	public static void main(String[] args) {
		SpringApplication.run(MockAaApplication.class, args);
	}
}
