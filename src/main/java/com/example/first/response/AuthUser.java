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
    private String nickName;
    private String name;
    private String birthDate;
    private String address;
    private String service;
    private String password;
    private Long authedUserId;
    private boolean authResult;

    @Builder
    public AuthUser(String email, Long postId, Long userId, String nickName, String name, String birthDate, String address, String service, String password, Long authedUserId, boolean authResult) {
        this.email = email;
        this.postId = postId;
        this.userId = userId;
        this.nickName = nickName;
        this.name = name;
        this.birthDate = birthDate;
        this.address = address;
        this.service = service;
        this.password = password;
        this.authedUserId = authedUserId;
        this.authResult = authResult;
    }
}
