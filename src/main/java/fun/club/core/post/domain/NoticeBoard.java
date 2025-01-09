package fun.club.core.post.domain;


import fun.club.core.comment.domain.Comment;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@DiscriminatorValue("noticeboard")
@Entity
@Getter
@NoArgsConstructor
public class NoticeBoard extends Board {

/**
 * 공지게시판에는 알림 기능을 설정하여 글이 올라오면 알림이 가게끔 설정
 */

    // 생성자를 도입해서 부모 클래스의 필드를 자식 클래스에서 쓰고 싶을 때 super 를 이용해서 생성자를 생성한다.
    @Builder
    public NoticeBoard(User writer,List<Comment> comments,PostDetails postDetails) {
        super(writer,comments,postDetails);
    }

}
/**
 * 두개의 보드와 댓글을 연관관계 매핑 하지 않은 이유는 게시글을 저장하는 구조이고 직접적으로 댓글을 포함하지 않으므로 postDetails 와 연관관계가 맺어지면 comment 와는 간접적으로 연관관계가 맺어진다.
 * 즉, 게시글을 통해 댓글을 접근할 수 있으므로 따로 맺지 않았다.
 */
// ADMIN 만 작성 가능(권한 관련은 api 에서)