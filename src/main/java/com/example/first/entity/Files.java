package com.example.first.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Files {

    private Long id;
    private Long postId;
    private String fileName;

    @Builder
    public Files(Long postId, String fileName) {
        this.postId = postId;
        this.fileName = fileName;
    }
}
