package com.example.first.service;

import com.example.first.entity.User;
import com.example.first.entity.UserSession;
import com.example.first.exception.*;
import com.example.first.repository.MybatisPostRepository;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final MybatisUserRepository userRepository;
    private final MybatisSessionRepository sessionRepository;
    private final MybatisPostRepository postRepository;
    private final PasswordEncoder passwordEncoder;

    // 로그인 기능
    public UserSession signIn(Login request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidSigninInformation::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidSigninInformation();
        }
        UserSession userSession = UserSession.builder()
                .userId(user.getId())
                .build();
        sessionRepository.save(userSession);
        return sessionRepository.findByAccessToken(userSession.getAccessToken()).orElseThrow(UnknownError::new);
    }

    public com.example.first.response.AuthUser authUser(com.example.first.response.AuthUser authUser) {
        switch (authUser.getService()) {
            case "post":
                if(authUser.getUserId().equals(authUser.getAuthedUserId())){
                    return com.example.first.response.AuthUser.builder()
                            .authResult(true)
                            .build();
                }else {
                    throw new Unauthorized("본인의 게시물만 수정할 수 있습니다.");
                }
            case "email":
                if(authUser.getEmail().equals("")){
                    throw new Unauthorized("이메일을 입력하세요.");
                }
                if (userRepository.findByEmail(authUser.getEmail()).isEmpty()) {
                    return com.example.first.response.AuthUser.builder()
                            .authResult(true)
                            .build();
                } else {
                    return com.example.first.response.AuthUser.builder()
                            .authResult(false)
                            .build();
                }
            case "name":
                if(authUser.getName().equals("")){
                    throw new Unauthorized("닉네임을 입력하세요.");
                }

                if(!userRepository.findByName(authUser.getName()).isEmpty()
                        && !userRepository.findByName(authUser.getName()).orElseThrow().getEmail().equals(authUser.getEmail())){
                    throw new DuplicateEmail("중복된 닉네임입니다.");
                }
                    return com.example.first.response.AuthUser.builder()
                            .name(authUser.getName())
                            .authResult(true)
                            .build();

            case "password":
                User user1 = userRepository.findById(authUser.getAuthedUserId()).orElseThrow(UserNotFound::new);
                if(passwordEncoder.matches(authUser.getPassword(), user1.getPassword())){
                    return com.example.first.response.AuthUser.builder()
                            .authResult(true)
                            .build();
                } else {
                    return com.example.first.response.AuthUser.builder()
                            .authResult(false)
                            .build();
                }
            case "login":
                return com.example.first.response.AuthUser.builder()
                    .authResult(true)
                    .build();
            case "auth":
                User authedUser =userRepository.findById(authUser.getAuthedUserId()).orElseThrow();
                return com.example.first.response.AuthUser.builder()
                        .email(authedUser.getEmail())
                        .name(authedUser.getName())
                        .authResult(true)
                        .build();

            default: throw new InvalidRequest();
        }
    }
}
