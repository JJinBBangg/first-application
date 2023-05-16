package com.example.first.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionResponse {
    private String accessToken;
    private String refreshToken;

    @Builder
    public SessionResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
