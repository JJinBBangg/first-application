package com.example.first.controller;


import com.example.first.response.AuthResponse;
import com.example.first.response.AuthUser;
import com.example.first.service.AuthService;
import com.example.first.service.KakaoLoginService;
import com.example.first.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Date;

@RestController
@Slf4j
@RequiredArgsConstructor
public class KakaoLoginController {
    @Value("${jtw.key}")
    public String key;

    private final KakaoLoginService kakaoLoginService;
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/app/login/kakao/{code}")
    public AuthResponse kakaoLogin(@PathVariable String code) {
        log.info("codeResponse:{}", code);
        // URL에 포함된 code를 이용하여 액세스 토큰 발급
        ResponseEntity<KakaoLoginService.LoginTokenResponse> tokenResponse = kakaoLoginService.getKakaoAccessToken(code);
        // 발급받은 토큰으로 사용자정보확인
        ResponseEntity<KakaoLoginService.LoginResultResponse> userInfoResponse = kakaoLoginService.getUserInfo(tokenResponse.getBody().getAccess_token());


        // email 확인 후 가입 시키는 서비스
        // email 존재할 경우 해당 email로 로그인
        String kakaoEmail = userInfoResponse.getBody().getKakao_account().getEmail();
        AuthUser authUserResponse = userService.kakaoEmailCheck(userInfoResponse.getBody().getKakao_account());
        

        String jws = Jwts.builder()
                .setSubject(String.valueOf(authUserResponse.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000L))) // 30분
                .compact();

        String refreshJws = Jwts.builder()
                .setSubject(String.valueOf(authUserResponse.getUserId()))
                .signWith(Keys.hmacShaKeyFor(Base64.getDecoder().decode(key)))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24L * 60L * 60L * 1000L))) // 30일
                .compact();


        return AuthResponse.builder()
                .accessToken(jws)
                .refreshToken(refreshJws)
                .build();
    }

    @GetMapping("/app/logout/kakao")
    public void kakaoLogout() {

    }

    @Data
    public static class CodeResponse {
        private String code;
        private String state;
        private String error;
        private String error_description;
    }
}
