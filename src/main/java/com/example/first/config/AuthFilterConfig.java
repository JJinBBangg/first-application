//package com.example.first.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class AuthFilterConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable();
////        http.formLogin(Customizer.withDefaults());
////        http.formLogin().loginPage("/login");
////        http.authorizeHttpRequests()
////                .requestMatchers("/api/**").authenticated()
////                .and()
////                .formLogin()
////                .loginPage("/auth/login")
////                .permitAll()
////                .and().logout()
////                .permitAll();
//        return http.build();
//    }
//
//}
