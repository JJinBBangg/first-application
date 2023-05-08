package com.example.first.service;

import com.example.first.entity.User;
import com.example.first.entity.UserSession;
import com.example.first.exception.InvalidSigninInformation;
import com.example.first.repository.MybatisSessionRepository;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MybatisUserRepository userRepository;
    private final MybatisSessionRepository sessionRepository;
    private final PasswordEncoder passwordEncoder;
    // 로그인 기능
    public UserSession signIn(Login request) {
        User user = userRepository.findByEmail(User.builder()
                        .email(request.getEmail())
                        .build())
                .orElseThrow(InvalidSigninInformation::new);
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new InvalidSigninInformation();
        }
        UserSession userSession = UserSession.builder()
                .userId(user.getId())
                .build();
        sessionRepository.save(userSession);
        return sessionRepository.findByAccessToken(userSession.getAccessToken()).orElseThrow(UnknownError::new);
    }
}
