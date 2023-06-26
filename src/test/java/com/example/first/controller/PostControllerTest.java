package com.example.first.controller;

import com.example.first.entity.Post;
import com.example.first.entity.User;
import com.example.first.repository.MybatisPostRepository;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.Login;
import com.example.first.request.PostCreate;
import com.example.first.request.PostEdit;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
        // 각 테스트(메서드)가 진행 될 때 마다 실행되는 method
    void clean() {
        sessionRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 Post를 DB에 저장한다.")

    void test1() throws Exception {
        User user =User.builder()
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

        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json1 = objectMapper.writeValueAsString(request);

        //excepted
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(json1)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""))
//                .andExpect(MockMvcResultMatchers.content().json(json))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 title값은 필수다.")
    void test2() throws Exception {

        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();
        String json = objectMapper.writeValueAsString(request);

        //excepted
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
//                .andExpect(jsonPath("$.code").value("400"))
//                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
//                .andExpect(jsonPath("$.validation.title").value("타이틀이 입력하세요."))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 DB에 Post 래코드 추가.")
    void test3() throws Exception {
        User user =User.builder()
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
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        String json1 = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(json1)
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //then
        assertEquals(1, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //iven
        User user = User.builder()
                .email("1234@naver.com")
                .name("kookjin123")
                .password("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .userId(user.getId())
                .build();

        postRepository.save(post);

        //expected
        mockMvc.perform(get("/posts/{id}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("123456789012345"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        User user = User.builder()
                .email("1234@naver.com")
                .name("kookjin123")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user);
        List<Post> requestPosts = IntStream.range(1, 151)
                .mapToObj(i -> Post.builder()
                        .title("제목" + i)
                        .content("내용" + i)
                        .userId(user.getId())
                        .build())
                .collect(Collectors.toList());

        postRepository.saveAll(requestPosts);
        //expected

        mockMvc.perform(get("/posts?page=0")
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                /**
                 * 단건조회인 경우 아래 json type으로 객체가 전달됭
                 * {id:..., title:...}
                 */
                /**
                 * 여러건 조회인 경우 List type으로 객체 전달됨
                 * [{id:..., title:...}, {id:..., title:...}]
                 */

                .andExpect(jsonPath("$.list.length()", Matchers.is(10)))
                .andExpect(jsonPath("$.list[0].title").value("제목150"))
                .andExpect(jsonPath("$.list[0].content").value("내용150"))
                .andExpect(jsonPath("$.list[1].title").value("제목149"))
                .andExpect(jsonPath("$.list[1].content").value("내용149"))
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("글 수정 조회")
    void test6() throws Exception {
        User user1 =User.builder()
                .name("kook123")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user1);

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
        //given
        User user = User.builder()
                .email("1234@naver.com")
                .name("kookjin123")
                .password("1234")
                .build();
        userRepository.save(user);
        Post post1 = Post.builder()
                .title("제목1")
                .content("내용1")
                .userId(user.getId())
                .build();


        postRepository.save(post1);
        Post post2 = Post.builder()
                .title("제목2")
                .content("내용2")
                .userId(user.getId())
                .build();


        postRepository.save(post2);

        PostEdit postEdit = PostEdit.builder()
                .title("제목3")
                .content("내용3")
                .build();
        //when
        //expected
        String edit= objectMapper.writeValueAsString(postEdit);
        mockMvc.perform(patch("/posts/{postId}", post2.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(edit))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(get("/posts?page=1&search=&type=all")
                .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.list[0].id").value(post2.getId()))
                .andExpect(jsonPath("$.list[0].title").value("제목3"))
                .andExpect(jsonPath("$.list[0].content").value("내용3"))
                .andExpect(jsonPath("$.list[1].id").value(post1.getId()))
                .andExpect(jsonPath("$.list[1].title").value("제목1"))
                .andExpect(jsonPath("$.list[1].content").value("내용1"))
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("글 삭제")
    void test7() throws Exception {
        User user1 =User.builder()
                .name("kook123")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user1);

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
        //given

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .userId(user1.getId())
                .build();


        postRepository.save(post);

        //when
        //expected

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("다른 사람의 글 삭제 시 오류")
    void test8() throws Exception {
        //user1으로 로그인 후 user2가 작성한 글을 user1이 삭제하려고하는 경우 오류
        User user1 =User.builder()
                .name("kook123")
                .email("kk1@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user1);

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
        //given
        User user2 =User.builder()
                .name("kook1123")
                .email("kk1123@naver.com")
                .password(passwordEncoder.encode("1234"))
                .build();
        userRepository.save(user2);

        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .userId(user2.getId())
                .build();


        postRepository.save(post);

        //when
        //expected

        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception{
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test10() throws Exception{
        User user =User.builder()
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

        PostEdit postEdit = PostEdit.builder()
                .title("제목1")
                .content("내용1")
                .build();
        //when
        //expected

        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .header("Authorization", accessToken)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }
    @Test
    @DisplayName("게시글 작성시 제목에 '바보'는 포함될 수 없다.")
    void test11() throws Exception {
        User user =User.builder()
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

        //given
        PostCreate request = PostCreate.builder()
                .title("나는 바보입니다.")
                .content("내용입니다.")
                .build();
        String json1 = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .header("Authorization", accessToken)
                        .contentType(APPLICATION_JSON)
                        .content(json1))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());

        //then
    }
    // API 문서 작성

    // GET
    // POST

    // 클라이언트 입장에서 어떤 API가 있는지 모름

    //spring RestDocs
    // 장점
    // 1. 운영중인 코드에 영향이 없음
    // 2. 변경된 기능에 대해 최신버전 유지가 가능
    //   - Test 케이스를 통해 실행 문서를 생성시킨다.

}