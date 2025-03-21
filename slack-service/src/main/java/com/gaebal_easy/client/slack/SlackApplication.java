package com.gaebal_easy.client.slack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
public class SlackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SlackApplication.class, args);
	}

}
