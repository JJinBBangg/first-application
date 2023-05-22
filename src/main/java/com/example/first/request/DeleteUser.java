package com.example.first.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteUser {
    private Long authedUserId;
    private String password;

    @Builder
    public DeleteUser(String password, Long authedUserId) {
        this.password = password;
        this.authedUserId = authedUserId;
    }
}
