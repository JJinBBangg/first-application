package com.example.first.service;


import com.example.first.entity.Files;
import com.example.first.entity.Post;
import com.example.first.entity.PostEditor;
import com.example.first.exception.PostNotFound;
import com.example.first.repository.MybatisPostRepository;
import com.example.first.request.PostCreate;
import com.example.first.request.PostEdit;
import com.example.first.response.PostResponse;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor // final로 생성된 field의 생성자 자동 autowired
@Transactional(rollbackFor = Exception.class)
@MultipartConfig(maxFileSize =2*1024*1024 ,maxRequestSize = 2*1024*1024*5)
public class PostService {

    private final MybatisPostRepository postRepository;

    private final S3Client s3;

    @Value("${aws.bucketName}")
    private String bucketName;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .userId(postCreate.getUserId())
                .build();
        postRepository.save(post);

        if (postCreate.files != null && postCreate.files.size() > 0) {
            postCreate.files.stream().forEach((file) -> {
                postRepository.saveFile(Files.builder()
                        .fileName(file.getOriginalFilename())
                        .postId(post.getId())
                        .build());

                String key = "post/"+post.getId()+"/" + file.getOriginalFilename();
                PutObjectRequest objectRequest = PutObjectRequest.builder()
                        .bucket(bucketName)
                        .acl(ObjectCannedACL.PUBLIC_READ)
                        .key(key)
                        .build();
                try {
                    s3.putObject(objectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public PostResponse get(Long id) {

        postRepository.hitPlus(id);
        Post post1 = postRepository.getPostWithFilesAndUser(id).orElseThrow(PostNotFound::new);
        log.info(">>>>{}", post1.getUser());
        return PostResponse.builder()
                .id(post1.getId())
                .title(post1.getTitle())
                .content(post1.getContent())
                .dateTime(post1.getDateTime())
                .name(post1.getUser().getName())
                .hit(post1.getHit())
                .build();
    }

    public List<PostResponse> getList(int page) {
        return postRepository.findPage(page).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder builder = post.toEditor();
        PostEditor postEditor = builder
                .title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();
        post.edit(postEditor);
        postRepository.edit(post);
    }

    public void delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFound::new);
        postRepository.delete(id);

    }
}
//            if(postEdit.getTitle() !=null){
//                builder.title(postEdit.getTitle());
//            }
//            if(postEdit.getContent()!=null){
//                builder.content(postEdit.getContent());
//            }
// 데이터 양이 많은 경우 -> 비용이 너무 많이 든다
// 글이 100,000,000 -> DB글 모두 조회하는 경우 -> DB가 뻗을 수 있다.
// DB -> 애플리캐이션 서버로 전달하는 시간, 트래픽비용 등이 많이 발생할 수 있다.
/**
 * Controller -> WebPostService(Client와 소통 응답)      -> Repository
 * PostService(외부 서비스와 연동되는 서비스)
 */
//                .map(post -> new PostResponse(post))
//                .map(post->{ PostResponse.builder()
//                        .id(post.getId())
//                        .title(post.getTitle())
//                        .content(post.getContent())
//                        .build()})
