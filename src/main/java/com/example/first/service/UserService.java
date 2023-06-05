package com.example.first.service;


import com.example.first.entity.User;
import com.example.first.exception.DuplicateEmail;
import com.example.first.exception.UserNotFound;
import com.example.first.repository.MybatisUserRepository;
import com.example.first.request.DeleteUser;
import com.example.first.request.UserCreate;
import com.example.first.request.UserEdit;
import com.example.first.response.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor // final로 생성된 field의 생성자 자동 autowired
@Transactional(rollbackFor = Exception.class)
public class UserService {

    private final MybatisUserRepository mybatisUserRepository;

    private final PasswordEncoder passwordEncoder;
    public User add(UserCreate userCreate) {
        if (!mybatisUserRepository.findByEmail(userCreate.getEmail()).isEmpty()) {
            throw new DuplicateEmail();
        }
        if (!mybatisUserRepository.findByName(userCreate.getName()).isEmpty()){
            throw new DuplicateEmail("중복된 닉네임입니다.");
        }
        User user = User.builder()
                .name(userCreate.getName())
                .email(userCreate.getEmail())
                .password(passwordEncoder.encode(userCreate.getPassword()))
                .build();
        mybatisUserRepository.save(user);
        return mybatisUserRepository.findById(user.getId()).orElseThrow(UserNotFound::new);
    }

    public List<User> getUsers(int page) {
        return mybatisUserRepository.findAll(page);
    }

    public UserResponse get(Long id) {
        User user = mybatisUserRepository.findById(id).orElseThrow(UserNotFound::new);
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .dateTime(user.getDateTime())
                .build();
    }

    public void edit(UserEdit userEdit) {
        if(!mybatisUserRepository.findByName(userEdit.getName()).isEmpty()
                && !mybatisUserRepository.findByName(userEdit.getName()).orElseThrow().getId().equals(userEdit.getUserId())){
           throw new DuplicateEmail("중복된 닉네임입니다.");
        };

    mybatisUserRepository.update(userEdit);

    }

    public void delete(DeleteUser deleteUser) {
        User user =mybatisUserRepository.findById(deleteUser.getAuthedUserId()).orElseThrow();
        if(!passwordEncoder.matches(deleteUser.getPassword(), user.getPassword())){
            throw new DuplicateEmail("이전비밀번호를 확인하세요.");
        }
        mybatisUserRepository.delete(user.getId());
    }
}
