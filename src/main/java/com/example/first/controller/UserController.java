package com.example.first.controller;

import com.example.first.entity.User;
import com.example.first.request.UserCreate;
import com.example.first.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public void addUser(UserCreate request){
        userService.add(request);
    }

    @GetMapping("/users")
    public List<User> get(){
        return userService.getUsers();
    }
}
