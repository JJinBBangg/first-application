package com.example.first.config;


import com.example.first.entity.UserSession;
import com.example.first.exception.Unauthorized;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.response.SessionResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;

@RequiredArgsConstructor
@Slf4j
@Component
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final MybatisSessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public UserSession resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info("AuthResolver 실행");
        String accessJws = webRequest.getHeader("Authorization");
        String refreshJws = webRequest.getHeader("RefreshToken");
        String jws = "";
        //refreshToken 검증 및 처리
        if(refreshJws != null && !refreshJws.equals("")){
           jws = webRequest.getHeader("RefreshToken");
           try{
               Jws<Claims> claims = Jwts.parserBuilder()
                        .setSigningKey(appConfig.getKey())
//                        .setAllowedClockSkewSeconds(60) // 1분까지는 시간차이를 허용
                        .build()
                        .parseClaimsJws(jws);
                String userId = claims.getBody().getSubject();
                return UserSession.builder()
                        .userId(Long.valueOf(userId))
                        .build();
           } catch (ExpiredJwtException e){
               throw new Unauthorized("로그인 인증이 만료되었습니다.");
           }
        } else if (accessJws == null || accessJws.equals("")) {
            throw new Unauthorized("로그인이 필요합니다.");
        } else {
            jws = accessJws;
        }
        // 일반 accessToken 검증 및 처리
        try {
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(appConfig.getKey())
//                .setAllowedClockSkewSeconds(60) // 1분까지는 시간차이를 허용
                .build()
                .parseClaimsJws(jws);
        String userId = claims.getBody().getSubject();
        return UserSession.builder()
                .userId(Long.valueOf(userId))
                .build();

        } catch (ExpiredJwtException e) {
            throw new Unauthorized("로그인 인증이 만료되었습니다.");
        } catch (JwtException e) {
            throw new Unauthorized("로그인이 필요합니다.");
        }
    }
}

//    @Override // 쿠키인증
//    public UserSession resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        if(request == null){
//            log.info("servletRequest null");
//            throw  new Unauthorized();
//        }
//
//        if(request.getCookies()==null || request.getCookies().length == 0){
//            log.info("쿠키가 없음");
//            throw  new Unauthorized();
//        }
//        Cookie[] cookies = request.getCookies();
//
//        String accessToken = cookies[1].getValue();
//        // 데이터베이스 사용자 확인작업
//        //
//        UserSession userSession = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);
//        return userSession;
//    }