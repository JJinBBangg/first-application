package com.example.first.request;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Login {
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
    private boolean refresh;

    @Builder
    public Login(String email, String password, boolean refresh) {
        this.email = email;
        this.password = password;
        this.refresh = refresh;
    }
}
