package com.example.first.controller;

import com.example.first.entity.User;
import com.example.first.entity.UserSession;
import com.example.first.exception.InvalidSigninInformation;
import com.example.first.exception.Unauthorized;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.repository.PostRepository;
import com.example.first.request.Login;
import com.example.first.response.SessionResponse;
import com.example.first.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login, HttpSession session){ // todo HttpServletResponse 활용법 따로 공부하기
        UserSession logedinUserSession= authService.signIn(login);
        // 로그인 정보 저장 미체크
        session.setAttribute("loggedInUser", logedinUserSession.getUserId());
        // 로그인 정보 저장 체크
        ResponseCookie cookie =ResponseCookie.from("SESSION", logedinUserSession.getAccessToken())
                .domain("localhost") //  todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }
    @PostMapping("/logout")
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
}

//    public ResponseEntity<Object> login(@RequestBody Login login, HttpSession session){ // todo HttpServletResponse 활용법 따로 공부하기
//        UserSession logedinUserSession= authService.signIn(login);
//        // 로그인 정보 저장 미체크
//        session.setAttribute("loggedInUser", logedinUserSession.getUserId());
//        // 로그인 정보 저장 체크
//        ResponseCookie cookie =ResponseCookie.from("SESSION", logedinUserSession.getAccessToken())
//                .domain("localhost") //  todo 서버 환경에 따른 분리 필요
//                .path("/")
//                .httpOnly(true)
//                .secure(false)
//                .maxAge(Duration.ofDays(30))
//                .sameSite("Strict")
//                .build();
//        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
//    } cookie 로 인증
