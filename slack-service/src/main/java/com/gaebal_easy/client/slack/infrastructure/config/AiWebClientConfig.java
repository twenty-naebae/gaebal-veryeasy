package com.gaebal_easy.client.slack.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AiWebClientConfig {

	@Bean
	public WebClient aiWebClient(WebClient.Builder builder) {
		return builder.baseUrl("https://generativelanguage.googleapis.com") // 기본 URL 설정
			.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
			.build();
	}
}

