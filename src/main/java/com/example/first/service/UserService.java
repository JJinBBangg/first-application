package com.example.first.service;


import com.example.first.entity.User;
import com.example.first.exception.DuplicateEmail;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.UserCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // final로 생성된 field의 생성자 자동 autowired
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final MybatisUserRepository mybatisUserRepository;

    public void add(UserCreate userCreate) {
       if(mybatisUserRepository.findByEmail(User.builder().email(userCreate.getEmail()).build())!=null) {
            throw new DuplicateEmail();
        }

        mybatisUserRepository.save(User.builder()
                        .name(userCreate.getName())
                        .email(userCreate.getEmail())
                        .password(userCreate.getPassword())
                        .build());
    }

    public List<User> getUsers() {
        return mybatisUserRepository.findAll();
    }
}
