package com.example.first.controller;

import com.example.first.entity.User;
import com.example.first.request.UserCreate;
import com.example.first.response.UserResponse;
import com.example.first.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    public User addUser(@RequestBody @Valid UserCreate userCreate){
        log.info("controller>>>{}", userCreate);
        return userService.add(userCreate);
    }
    @GetMapping("/user")
    public List<User> getList(@RequestParam(defaultValue = "1") int page){
        return userService.getUsers(page);
    }
    @GetMapping("/user/{id}")
    public UserResponse get(@PathVariable Long id){
        return userService.get(id);
    }

}
