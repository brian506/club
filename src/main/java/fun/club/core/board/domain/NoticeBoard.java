package fun.club.core.board.domain;


import fun.club.core.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Table(name = "NOTICEBOARD")
@Entity
@Getter
@RequiredArgsConstructor
public class NoticeBoard {

    @Id @GeneratedValue
    @Column(name = "noticeBoard_id")
    private Long id;

    @OneToMany(mappedBy = "noticeBoard", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts;

}
/**
 * 두개의 보드와 댓글을 연관관계 매핑 하지 않은 이유는 게시글을 저장하는 구조이고 직접적으로 댓글을 포함하지 않으므로 post 와 연관관계가 맺어지면 comment 와는 간접적으로 연관관계가 맺어진다.
 * 즉, 게시글을 통해 댓글을 접근할 수 있으므로 따로 맺지 않았다.
 */