package com.example.first.controller;

import com.example.first.entity.Post;
import com.example.first.entity.User;
import com.example.first.repository.MybatisPostRepository;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.repository.PostRepository;
import com.example.first.request.PostCreate;
import com.example.first.request.UserCreate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MybatisPostRepository postRepository;
    @Autowired
    private MybatisUserRepository userRepository;
    @Autowired
    private MybatisSessionRepository sessionRepository;

    @BeforeEach
        // 각 테스트(메서드)가 진행 될 때 마다 실행되는 method
    void clean() {
        sessionRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입")
    void test1() throws Exception {
        UserCreate request = UserCreate.builder()
                .email("kkk@naver.com")
                .name("koko")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/user")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("중복된 E-mail 회원가입")
    void test2() throws Exception {
        userRepository.save(User.builder()
                .email("kkk@naver.com")
                .password("1234")
                .name("koko")
                .build());

        UserCreate request = UserCreate.builder()
                .email("kkk@naver.com")
                .name("koko")
                .password("1234")
                .build();


        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/user")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("회원 1명 검색")
    void test3() throws Exception {
       User user = User.builder()
               .email("kkk@naver.com")
               .password("1234")
               .name("koko")
               .build();
        userRepository.save(user);

        mockMvc.perform(get("/user/{id}", user.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.email").value("kkk@naver.com"))
                .andExpect(jsonPath("$.name").value("koko"));

    }
    @Test
    @DisplayName("회원 여러명 검색")
    void test4() throws Exception {
        List<User> requestUsers = IntStream.range(1, 31)
                .mapToObj(i -> User.builder()
                        .email("kk"+i+"@naver.com")
                        .name("kk"+i)
                        .password("1234")
                        .build())
                .collect(Collectors.toList());
     userRepository.saveAll(requestUsers);

        mockMvc.perform(get("/user?page=0")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(30)))
                .andDo(MockMvcResultHandlers.print());
    }
}