package com.example.first.controller;

import com.example.first.entity.Region;
import com.example.first.request.WeatherRequest;
import com.example.first.response.WeatherResponse;
import com.example.first.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class WeatherApiController {


    private final WeatherService weatherService;

    @PostMapping("/region")
    public ResponseEntity<String> resetRegionList() {
        String fileLocation = "storage/init/regionList.csv"; // 설정파일에 설정된 경로 뒤에 붙인다
        Path path = Paths.get(fileLocation);
        URI uri = path.toUri();
        List<Region> regionList = new ArrayList<Region>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new UrlResource(uri).getInputStream()))
        ) {
            String line = br.readLine(); // head 떼기
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                Region build = Region.builder()
                        .regionParent(split[1])
                        .regionChild(split[2])
                        .nx((split[3]))
                        .ny((split[4]))
                        .build();
                regionList.add(build);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        weatherService.addRegion(regionList);
        return ResponseEntity.ok("초기화에 성공했습니다");
    }

    @GetMapping("/weather")
    public List<WeatherResponse> get(String region){
        log.info(">>>>>{}",region);
        return weatherService.getWeatherInfo(
                WeatherRequest.builder()
                .region(region)
                .build());
    }
}
