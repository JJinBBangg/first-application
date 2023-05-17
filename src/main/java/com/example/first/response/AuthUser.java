package com.example.first.response;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class AuthUser {
    private String email;
    private Long postId;
    private Long userId;
    private String name;
    private String service;
    private String password;
    private Long authedUserId;
    private boolean authResult;

    @Builder
    public AuthUser(String email, Long postId, Long userId, String service,String name, Long authedUserId, boolean authResult, String password) {
        this.email = email;
        this.postId = postId;
        this.service = service;
        this.userId = userId;
        this.password = password;
        this.authedUserId = authedUserId;
        this.authResult = authResult;
        this.name = name;
    }
}
