package com.example.first.controller;

import com.example.first.entity.User;
import com.example.first.entity.UserSession;
import com.example.first.exception.UserNotFound;
import com.example.first.repository.MybatisPostRepository;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import jakarta.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.javassist.NotFoundException;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.CookieResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MybatisUserRepository userRepository;

    @Autowired
    private MybatisSessionRepository sessionRepository;

    @Autowired
    private MybatisPostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
        // 각 테스트(메서드)가 진행 될 때 마다 실행되는 method
    void clean() {
        postRepository.deleteAll();
        sessionRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {

        //given
        User user = User.builder()
                .name("kook")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("kk1@naver.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);

        //excepted
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().string(""))
//                .andExpect(MockMvcResultMatchers.content().json(json))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("로그인 성공 후 JWT토큰 발행")
    void test2() throws Exception {
        User user = User.builder()
                .name("kook")
                .email("kk1123@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);

        //given
        Login login = Login.builder()
                .email("kk1123@naver.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);
//        User user1 = userRepository.getUserWithSession(user.getId()).orElseThrow(UserNotFound::new);

        //excepted
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 후 권한이 필요한 페이지접속")
    void test3() throws Exception {
        //given
        User user = User.builder()
                .name("kook123")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("kk1@naver.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();

        String accessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");


        mockMvc.perform(post("/auth/user/login")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.authResult").value(true))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 후 잘못된 권한으로 페이지 접속")
    void test4() throws Exception {
        //given
        User user = User.builder()
                .name("kook123")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);

        Login login = Login.builder()
                .email("kk1@naver.com")
                .password("1234")
                .build();
        String json = objectMapper.writeValueAsString(login);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andReturn();

        String accessToken = JsonPath.read(result.getResponse().getContentAsString(), "$.accessToken");


        mockMvc.perform(post("/auth/user/login")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken+3)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }
}