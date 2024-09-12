package fun.club.core.post.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Embeddable
@Getter
@Setter
public class PostDetails {

    private String title;
    private String content;
    private String file;
    // DB 에서는 String 으로 저장됨
    // multipartFile 은 파일 업로드 시 사용되는 객체
}
//    protected PostDetails() {}
//
//    public PostDetails(String title, String content, String image) {
//        this.title = title;
//        this.content = content;
//        this.image = image;
//    } 임베비드 필드값들의 상태가 변하면 안될 때 위에 두 개 생성




