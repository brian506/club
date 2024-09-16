package fun.club.core.post.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import fun.club.common.base.BaseTimeEntity;
import fun.club.core.comment.domain.Comment;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") // 싱글 테이블이기 때문에 d-type 을 이용해서 구분해야 테이블을 구분할 수 있음
@Entity
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Board extends BaseTimeEntity {

    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User writer;

    @Embedded
    @Setter
    private PostDetails postDetails;

    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();



    /**
     * -- SETTER --
     *  외래 키 관리로 각각의 board 는 해당 게시물 내용이 DB 에서는 보여지지 않는다.
     *  처음에는 각각의 board 에서도 필드값들에 대한 내용들이 보여져야 하지 않을까? 라는 생각을 했지만
     *  값들이 postDetails 에도 있고 board 에도 있게 되면 중복으로 저장되는 것이므로 비효율적이다.
     *  그래서 어느 board 에 저장할지는 Id 값으로 찾아준다.
     */

}
