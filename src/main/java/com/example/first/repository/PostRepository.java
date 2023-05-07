package com.example.first.repository;


import com.example.first.entity.Post;

import java.util.List;
import java.util.Optional;

public interface PostRepository {


    int save(Post post);

    int count();

    int deleteAll();

    List<Post> findPage(int page);

    List<Post> findAll();

    Optional<Post> findById(Long id);

    int saveAll(List<Post> posts);

    int edit (Post post);

    int delete(Long id);
}
