package com.example.first.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@Slf4j
public class KakaoLoginService {
    private String requestURI = "https://kauth.kakao.com";
    private String redirectURI = "http://localhost:5173/kakaologin";
    private String apiURI = "https://kapi.kakao.com";
    @Value("${api.key}")
    private String client_id;

    public ResponseEntity<LoginTokenResponse> getKakaoAccessToken(String code) {
        URI uri = UriComponentsBuilder
                .fromUriString(requestURI)
                .path("/oauth/token")
                .encode()
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", client_id)
                .queryParam("redirect_uri", redirectURI)
                .queryParam("code", code)
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("", "");

        RequestEntity<MultiValueMap<String, String>> request = RequestEntity
                .post(uri)
                .headers(headers)
                .body(params);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LoginTokenResponse> response = restTemplate.exchange(request, LoginTokenResponse.class);
        return response;
    }

    public ResponseEntity<LoginResultResponse> getUserInfo(String accessToken) {
        URI uri = UriComponentsBuilder
                .fromUriString(apiURI)
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        RequestEntity request = RequestEntity
                .get(uri)
                .headers(headers)
                .build();
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<LoginResultResponse> response = restTemplate.exchange(request, LoginResultResponse.class);
        return response;

    }

    @Data
    public static class LoginResultResponse {
        private String id;
        private String connected_at;
        private KakaoAccount kakao_account;


    }
    @Data
    public static class KakaoAccount {
        private Profile profile;
        private String name;
        private String email;
        private String birthday;
        private String gender;
    }
    @Data
    public static class Profile {
        private String nickname;
    }

    @Data
    public static class LoginTokenResponse {
        private String token_type;
        private String access_token;
        private String id_token;
        private String expires_in;
        private String refresh_token;
        private String refresh_token_expires_in;
        private String scope;
    }
}
