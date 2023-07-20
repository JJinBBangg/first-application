package com.example.first.request;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class WeatherRequest {

    public String region;

    @Builder
    public WeatherRequest(String region) {
        this.region = region;
    }
}
