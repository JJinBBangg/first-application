package com.example.first.response;


import com.example.first.entity.Post;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 서비스 정책에 맞는 응답 클래스 생성
 */

@Getter
@NoArgsConstructor
public class PostResponse {
    private Long id;
    private String name;
    private String title;
    private String content;
    private Long userId;
    private LocalDateTime dateTime;
    private Long hit;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.hit = post.getHit();
        this.dateTime = post.getDateTime();
        this.name =post.getUser().get(0).getName();
        this.userId = post.getUser().get(0).getId();
    }

    @Builder
    public PostResponse(Long id, String title, String content, LocalDateTime dateTime, String name, long hit, Long userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.userId = userId;
        this.name = name;
        this.hit = hit;
    }

}
