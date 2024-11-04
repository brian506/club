package fun.club.core.post.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;



@Embeddable
@Getter
@Builder
public class PostDetails {

    private String title;
    private String content;
    @Setter
    private String file;


    protected PostDetails() {
    }

    public PostDetails(String title, String content, String file) {
        this.title = title;
        this.content = content;
        this.file = file;
    } // 임베비드 필드값들의 상태가 변하면 안될 때 위에 두 개 생성

}

/**
 * Setter 를 사용하면 좋지 않은 이유?
 * 1. Setter 를 허용하면 객체의 상태가 언제든지 변경될 수 있기 때문에 불변성을 유지하기 어렵다.
 * 2. Setter 는 객체의 상태를 외부에서 변경할 수 있게 하기 때문에 예상치 못한 상태 변화가 일어난다.
 * 해결 방법 : 이를 Builder 패턴을 이용해서 각 클래스의 필드값들을 지정해주자.
 */



