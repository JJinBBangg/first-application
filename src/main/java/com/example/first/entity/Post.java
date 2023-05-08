package com.example.first.entity;


import com.example.first.request.PostEdit;
import jakarta.annotation.Nullable;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    private Long id;
    private String title;
    private String content;
    @Nullable
    private Long userId;
    private LocalDateTime dateTime;
    private Long hit;
    @Nullable
    private List<Files> files;
    private List<User> user;

    @Builder
    public Post(Long id,String title, String content, Long userId, List<Files> files,LocalDateTime dateTime, Long hit, List<User> user) {
        this.id = id;
        this.title= title;
        this.content=content;
        this.userId= userId;
        this.dateTime = dateTime;
        this.files = files;
        this.hit = hit;
        this.user =user;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return  PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }
}
