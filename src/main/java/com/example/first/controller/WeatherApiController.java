package com.example.first.controller;

import com.example.first.request.WeatherRequest;
import com.example.first.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeatherApiController {

    private final WeatherService weatherService;
    @GetMapping
    public String get(WeatherRequest request){
        weatherService.getWeatherInfo(request);
        return null;
    }

}
