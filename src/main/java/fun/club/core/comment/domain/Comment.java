package fun.club.core.comment.domain;

import fun.club.common.base.BaseTimeEntity;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.PostDetails;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "COMMENT")
@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public void update(String comment) {
        this.comment = comment;
    }



}
