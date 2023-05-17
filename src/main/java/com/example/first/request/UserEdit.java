package com.example.first.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEdit {
    private String name;
    private String password;
    private Long userId;

    @Builder
    public UserEdit(Long userId, String password, String name) {
        this.password = password;
        this.name = name;
        this.userId = userId;
    }

}
