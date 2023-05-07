package com.example.first.request;

import com.example.first.exception.InvalidRequest;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
public class PostCreate {
    @NotBlank(message = "타이틀이 입력하세요.")
    public String title;
    @NotBlank(message = "콘텐츠가 없습니다.")
    public String content;
    public Long userId;
    public LocalDateTime dateTime;
    @Nullable
    public List<MultipartFile> files;

    @Builder // 사용이유 1. 가독성이 좋다, 2. 필요한 값만 셋팅할 수 있다. 3. 객체의 불변성
    // 객체를 immutable 하게 생성한다면 생성 시점 이후 해당 객체의 상태를 변경할 수 없으므로,
    // 실행 중인 쓰레드 간 서로의 간섭에 의해 생길 수 있는 동기화(synchronization)
    // 문제에 대한 걱정을 안 해도 되게끔 해줍니다. (이것을 바로 Thread safe 하다고 함.)
    public PostCreate(long id, String title, String content,Long userId, List<MultipartFile> files) {
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.files = files;
    }
    public void validate(){
        if(title.contains("바보")){
            throw new InvalidRequest("title","제목에 '바보'를 포함할 수 없습니다.");
        } else if(content.contains("")){

        }
    }
}
