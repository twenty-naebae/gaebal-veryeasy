package com.gaebal_easy.delivery.application.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class NaverDirectionApiService {
    // @Value("${naver.direction5.client-id}")
    // private String clientId;
    //
    // @Value("${naver.direction5.client-secret}")
    // private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getDirection(double startLng, double startLat, double endLng, double endLat) {
        // System.out.println("클라이언트 아이디"+clientId+" "+clientSecret);
        log.warn("delivery!!!!!!"+startLng + "," + startLat + "," + endLng + "," + endLat);
        String url = UriComponentsBuilder.fromUriString("https://maps.apigw.ntruss.com/map-direction/v1/driving")
            .queryParam("start", startLng + "," + startLat)
            .queryParam("goal", endLng + "," + endLat)
            .queryParam("option", "trafast")
            .build()
            .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", "4zv0khteil");
        headers.set("X-NCP-APIGW-API-KEY", "ZOasLRxv66DyVEGjT6o0pbjSyngq8hjt1CK3Avg9");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return response.getBody();
    }
}
