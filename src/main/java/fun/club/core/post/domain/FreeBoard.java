package fun.club.core.post.domain;


import fun.club.core.comment.domain.Comment;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@DiscriminatorValue("freeboard") // dtype 으로 조회하는 것
@Entity
@Getter
@NoArgsConstructor
public class FreeBoard extends Board{

    /**
     * super() 를 사용해서 상속 클래스인 Board 의 필드값을 파라미터로 받아와서 Builder 로 쓸 수 있게 한 것
     */
    @Builder
    public FreeBoard(User writer,List<Comment> comments,PostDetails postDetails) {
        super(writer,comments,postDetails);
    }
}
