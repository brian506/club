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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User writer;


    @OneToMany(mappedBy = "board")
    private List<Comment> comments = new ArrayList<>();

    @Setter
    private int views; // 조회수

    @Embedded
    private PostDetails postDetails;

    protected Board(User writer,List<Comment> comments,PostDetails postDetails){
        this.writer = writer;
        this.comments = comments;
        this.postDetails = postDetails != null ? postDetails : PostDetails.builder().build();
    }
    /**
     * -- SETTER --
     *  외래 키 관리로 각각의 board 는 해당 게시물 내용이 DB 에서는 보여지지 않는다.
     *  처음에는 각각의 board 에서도 필드값들에 대한 내용들이 보여져야 하지 않을까? 라는 생각을 했지만
     *  값들이 postDetails 에도 있고 board 에도 있게 되면 중복으로 저장되는 것이므로 비효율적이다.
     *  그래서 어느 board 에 저장할지는 Id 값으로 찾아준다.
     */


/** postDetails 블로그에서 작성하자
 orphanRemoval 와 cascade ?
 *  cascade 는 부모 엔티티를 삭제하면 자식 엔티티도 같이 삭제하지만, 부모 엔티티에서 자식 엔티티르 삭제할 때는 완전히 삭제되지 않는다.
 *  그래서 orphanRemoval 을 통해 부모 엔티티에서 자식 엔티티를 삭제하기 위해서 사용한다.
 *  여기에서는 회원이 탈퇴하면 게시글도 자동으로 삭제되기 위해서는 cascade 가 필요한 거고 회원이 남아있는 상태에서 게시글 엔티티만 삭제하기 위해서는 orphanRemoval 가 필요한 것
 */

/**
 * 부모와 자식 엔티티, 연관관계에서의 주인 결정은 다르다
 *
 * 부모와 자식을 결정하는 요인은 엔티티의 주도성과 다른 엔티티의 생명 주기를 관리하는 엔티티가 된다.
 * 보통 다대일 관계에서 '일'인 쪽이 부모 엔티티가 된다.
 * 이와 관련된 cascade,orphanRemoval 등이 부모 클래스에서 자식 클래스를 매핑할 때 사용된다. 그러므로 부모 클래스에 설정한다.
 *
 * 연관관계에서의 주인을 결정하는 요인은 보통 다대일 관계에서 '다'인 쪽이 외래키를 관리하는 주인으로 지정한다.
 *
 * 보통 fetchType 은 부모클래스에서 설정하지만,
 * board 가 주인 클래스일 떄, postDetails 를 조회할 때 user 정보가 필요한 경우에만 DB에서 가져오도록 board 클래스에서 설정하는 것이 성능 최적화에 좋다.
 * 그래서 보통 ~XToOne 일 때 Fetch 를 설정한다.
 */

}
