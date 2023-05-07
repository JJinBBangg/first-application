package com.example.first.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class UserCreate {
    @NotBlank(message = "닉네임을 입력하세요.")
    public String name;
    @NotBlank(message = "e-mail을 입력하세요.")
    public String email;
    @NotBlank(message = "비밀번호를 입력하세요.")
    public String password;

    @Builder
    public UserCreate(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
