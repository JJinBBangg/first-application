package com.example.first.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSession {
    private Long id;
    private String accessToken;
    private Long userId;

    @Builder
    public UserSession(Long userId){
        this.accessToken = UUID.randomUUID().toString();
        this.userId = userId;
    }
}
