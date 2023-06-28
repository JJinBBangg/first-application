package com.example.first.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUser {
    private Long id;
    private String accessToken;
    private Long userId;

    @Builder
    public AuthUser(Long userId){
        this.accessToken = UUID.randomUUID().toString();
        this.userId = userId;
;
    }
}
