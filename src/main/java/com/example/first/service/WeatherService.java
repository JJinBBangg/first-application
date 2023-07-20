package com.example.first.service;

import com.example.first.entity.Region;
import com.example.first.entity.WeatherData;
import com.example.first.exception.RegionNotFound;
import com.example.first.repository.MybatisRegionRepository;
import com.example.first.repository.MybatisWeatherDataRepository;
import com.example.first.request.WeatherRequest;
import com.example.first.response.WeatherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weatherAPI.serviceKey}")
    private String serviceKey;

    private final String URI = "https://apis.data.go.kr";

    private final MybatisRegionRepository resionRepository;

    private final MybatisWeatherDataRepository weatherDataRepository;

    public void addRegion(List<Region> regionList) {
        resionRepository.deleteAll();
        resionRepository.saveAll(regionList);
    }

    @Transactional
    public List<WeatherResponse> getWeatherInfo(WeatherRequest request) {
        //요청지역 조회
        Region region = resionRepository.findByRegion(request.getRegion()).orElseThrow(RegionNotFound::new);
        log.info("지역1: {}, 지역2 : {}, nx : {}, ny : {} ", region.getRegionParent(), region.getRegionChild(), region.getNx(), region.getNy());
        //요청시각 조회
        LocalDateTime now = LocalDateTime.now();
        String yyyyMMdd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        int hour = now.getHour();
        int min = now.getMinute();
        if (min <= 30) { // 해당 시각 발표 전에는 자료가 없음 - 이전시각을 기준으로 해야함
            hour -= 1;
        }
        String hourStr = hour < 10 ? "0" + hour + "00" : hour + "00"; // 정시 기준
        String nx = region.getNx();
        String ny = region.getNy();
        String currentChangeTime = now.format(DateTimeFormatter.ofPattern("yy.MM.dd ")) + hour;

        //URI 생성
        URI uri = getUri(yyyyMMdd, "0500", nx, ny);

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RequestEntity request1 = RequestEntity
                .get(uri)
                .headers(headers)
                .build();

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        System.out.println(" uri = " + uri);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET , request1, String.class);
        String data = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        List<WeatherResponse> weatherResponses = WeatherDataParse(data, objectMapper);


        return weatherResponses;

    }

    private static List<WeatherResponse> WeatherDataParse(String data, ObjectMapper objectMapper) {
        try {
            WeatherRes data1 = objectMapper.readValue(data, WeatherRes.class);
            List<Item> item = data1.getResponse().getBody().getItems().getItem();
            List<WeatherResponse> list = new ArrayList<>();
            for (int i = 0; i < item.size() / 12; i++) {
                WeatherResponse weather = new WeatherResponse();
                String fcstDate = item.get((12 * i)).fcstDate;
                String fcstTime = item.get((12 * i)).fcstTime;
                weather.setDateTime(fcstTime);
                weather.setDate(fcstDate);
                for (int j = 0; j < 12; j++) {
                    switch (item.get((12 * i) + j).category) {
                        case "TMP":
                            weather.setTMP(item.get((12 * i) + j).fcstValue);
                            break;
                        case "POP":
                            weather.setPOP(item.get((12 * i) + j).fcstValue);
                            break;
                        case "PCP":
                            weather.setPCP(item.get((12 * i) + j).fcstValue);
                            break;
                        case "PTY":
                            weather.setPTY(item.get((12 * i) + j).fcstValue);
                            break;
                        case "REH":
                            weather.setREH(item.get((12 * i) + j).fcstValue);
                            break;
                        case "SNO":
                            weather.setSNO(item.get((12 * i) + j).fcstValue);
                            break;
                        default:
                            break;
                    }
                }
                list.add(weather);
            }
            return list;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private URI getUri(String yyyyMMdd, String hourStr, String nx, String ny) {

        System.out.println("encodedServiceKey = " + serviceKey);
        String encodedUri = URI + "/1360000/VilageFcstInfoService_2.0/getVilageFcst" +
                "?ServiceKey=" + serviceKey +
                "&numOfRows=1000" +
                "&pageNo=1" +
                "&dataType=JSON" +
                "&base_date=" + yyyyMMdd +
                "&base_time=" + hourStr +
                "&nx=" + nx +
                "&ny=" + ny;
        URI uri= java.net.URI.create(encodedUri);
        return uri;
    }


    @Data
    public static class WeatherRes {
        private Response response;
    }

    @Data
    public static class Response {
        private Header header;
        private Body body;
    }

    @Data
    public static class Body {
        String dataType;
        String pageNo;
        String numOfRows;
        String totalCount;
        Items items;
    }

    @Data
    public static class Items {
        List<Item> item;
    }

    @Data
    public static class Item {
        String baseDate;
        String baseTime;
        String category;
        String fcstDate;
        String fcstTime;
        String fcstValue;
        int nx;
        int ny;
    }
    @Data
    public static class Header {
        private String resultCode;
        private String resultMsg;
    }
}


//    StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
//        try {
//                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + serviceKey);
//                urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
//                urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
//                urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
//                urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(yyyyMMdd, "UTF-8")); /*‘21년 6월 28일 발표*/
//                urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hourStr, "UTF-8")); /*06시 발표(정시단위) */
//                urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
//                urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/
//
//                URL url = new URL(urlBuilder.toString());
//                log.info("request url: {}", url);
//
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Content-type", "application/json");
//
//                BufferedReader rd;
//                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                } else {
//                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                }
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = rd.readLine()) != null) {
//                sb.append(line);
//                }
//                rd.close();
//                conn.disconnect();
//                String data = sb.toString();
//                System.out.println("data = " + data);
//                } catch (IOException e) {
//                e.printStackTrace();
//                }