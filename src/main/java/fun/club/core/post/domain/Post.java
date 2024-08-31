package fun.club.core.post.domain;

import fun.club.common.base.BaseTimeEntity;
import fun.club.core.board.domain.FreeBoard;
import fun.club.core.board.domain.NoticeBoard;
import fun.club.core.comment.domain.Comment;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "POST")
@Entity
@Getter
@RequiredArgsConstructor
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writer;

    private String title;

    private String content;

    private String postUrl;

    @OneToMany(mappedBy = "post")
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "freeboard_id")
    private FreeBoard freeBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "noticeboard_id")
    private NoticeBoard noticeBoard;




}
