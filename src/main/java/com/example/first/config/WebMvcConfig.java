package com.example.first.config;

import com.example.first.repository.MybatisSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final MybatisSessionRepository sessionRepository;
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new Authintercetor())
//                .excludePathPatterns("/error", "favicon.ico");
////                .excludePathPatterns("/test"); // 인터셉터에 인증되지 않도록 제외시킴 뛰어넘음 prehandle 실행안됨
////                .addPathPatterns() // 페턴 추가 등록
//    }


    // 아규먼트 리졸버를 이용하여 로그인 검증

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new AuthResolver(sessionRepository));
    }
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**")
//                .allowedOrigins("http://localhost:5173");
//    }

//    @Override // 뷰로 던질 주소찾아가는 메소드
//    public void addViewControllers(ViewControllerRegistry registry) {
//        WebMvcConfigurer.super.addViewControllers(registry);
//    }
}
