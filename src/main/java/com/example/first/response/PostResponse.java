package com.example.first.response;


import com.example.first.entity.Post;
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
    private LocalDateTime dateTime;
    private Long hit;

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content, LocalDateTime dateTime, String name, long hit) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.dateTime = dateTime;
        this.name = name;
        this.hit = hit;
    }

}
