package com.example.first.controller;

import com.example.first.config.AppConfig;
import com.example.first.entity.AuthUser;
import com.example.first.request.Login;
import com.example.first.response.SessionResponse;
import com.example.first.service.AuthService;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        AuthUser logedinAuthUser = authService.signIn(login);

        String jws = Jwts.builder()
                .setSubject(String.valueOf(logedinAuthUser.getUserId()))
                .signWith(appConfig.getKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30 * 60 * 1000L))) // 30분
                .compact();

        String refreshJws = Jwts.builder()
                .setSubject(String.valueOf(logedinAuthUser.getUserId()))
                .signWith(appConfig.getKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24L * 60L * 60L * 1000L))) // 30일
                .compact();

        return SessionResponse.builder()
                .accessToken(jws)
                .refreshToken(refreshJws)
                .build();
    }

    @PostMapping("/auth/login/refresh")
    public SessionResponse refresh(AuthUser authUser) {

        String jws = Jwts.builder()
                .setSubject(String.valueOf(authUser.getUserId()))
                .signWith(appConfig.getKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (1800L * 1000L))) // 30분
                .compact();

        String refreshJws = Jwts.builder()
                .setSubject(String.valueOf(authUser.getUserId()))
                .signWith(appConfig.getKey())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (30L * 24L * 60L * 60L * 1000L))) // 30일
                .compact();

        return SessionResponse.builder()
                .accessToken(jws)
                .refreshToken(refreshJws)
                .build();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        // 클라이언트의 세션을 무효화하여 로그아웃 처리
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 쿠키를 삭제하여 클라이언트의 세션 쿠키도 제거
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return ResponseEntity.ok("로그아웃되었습니다.");
    }

    @PostMapping("/auth/user/{service}")
    public com.example.first.response.AuthUser authUser(@RequestBody com.example.first.response.AuthUser authUser, @PathVariable String service, AuthUser userSession) {
        log.info(">>>auth/user/ {}", service);
        com.example.first.response.AuthUser authedUser = authService.authUser(com.example.first.response.AuthUser.builder()
                .email(authUser.getEmail())
                .postId(authUser.getPostId())
                .userId(authUser.getUserId())
                .password(authUser.getPassword())
                .service(service)
                .name(authUser.getName())
                .authedUserId(userSession.getUserId())
                .build());
        return authedUser;
    }

    @PostMapping("/auth/signup/{service}")
    public com.example.first.response.AuthUser authUser(@RequestBody com.example.first.response.AuthUser authUser, @PathVariable String service) {
        log.info("auth/signup/ {}", service);
        com.example.first.response.AuthUser authedUser = authService.authUser(com.example.first.response.AuthUser.builder()
                .email(authUser.getEmail())
                .name(authUser.getName())
                .service(service)
                .build());
        return authedUser;
    }

//    @PostMapping("/auth/login")
//    public ResponseEntity<Object> login(@RequestBody Login login, HttpSession session){ // todo HttpServletResponse 활용법 따로 공부하기
//        UserSession logedinUserSession= authService.signIn(login);
//        // 로그인 정보 저장 미체크
//        session.setAttribute("loggedInUser", logedinUserSession.getUserId());
//        // 로그인 정보 저장 체크
//        ResponseCookie cookie =ResponseCookie.from("SESSION", logedinUserSession.getAccessToken())
//                .domain("localhost") //  todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
//    }
}



