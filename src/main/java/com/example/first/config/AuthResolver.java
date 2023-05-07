package com.example.first.config;


import com.example.first.entity.UserSession;
import com.example.first.exception.InvalidSigninInformation;
import com.example.first.exception.Unauthorized;
import com.example.first.repository.MybatisSessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final MybatisSessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
       return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public UserSession resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if(request == null){
            log.info("servletRequest null");
            throw  new Unauthorized();
        }
        Cookie[] cookies = request.getCookies();
        if(cookies.length == 0){
            log.info("쿠키가 없음");
            throw  new Unauthorized();
        }
        String accessToken = cookies[0].getValue();
        // 데이터베이스 사용자 확인작업
        //
        UserSession userSession = sessionRepository.findByAccessToken(accessToken).orElseThrow(Unauthorized::new);
        return userSession;
    }
}
