package com.example.first.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserResponse {

    private Long id;
    private String name;
    private String email;
    private LocalDateTime dateTime;


    @Builder
    public UserResponse(Long id, String name, String email, LocalDateTime dateTime) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dateTime = dateTime;
    }
}
