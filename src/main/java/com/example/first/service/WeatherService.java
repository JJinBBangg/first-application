package com.example.first.service;

import com.example.first.request.WeatherRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class WeatherService {

    private final String URI = "http://apis.data.go.kr";
    public ResponseEntity<> getWeatherInfo(WeatherRequest request) {
        URI uri = UriComponentsBuilder
                .fromUriString(URI)
                .path("/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .queryParam("ServiceKey", "")
                .queryParam("pageNo", "")
                .queryParam("numOfRows", "")
                .queryParam("dataType", "")
                .queryParam("base_date", "")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer ");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        RequestEntity requestEntity = RequestEntity
                .get(uri)
                .headers(headers)
                .build();
        RestTemplate restTemplate = new RestTemplate();

//        ResponseEntity<> response = restTemplate.exchange(requestEntity, HttpMethod.GET);
        return null;

    }

}
