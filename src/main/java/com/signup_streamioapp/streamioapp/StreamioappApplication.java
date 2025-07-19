package com.signup_streamioapp.streamioapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync // ✅ Enables support for @Async methods
public class StreamioappApplication {

	public static void main(String[] args) {
		SpringApplication.run(StreamioappApplication.class, args);
	}
}
