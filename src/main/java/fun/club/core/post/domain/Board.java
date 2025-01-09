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

    /**
     * 추상 클래스인 Board 에서 상속 클래스인 noticeBoard, freeBoard 가 Board 의 필드값을 사용할 수 있게 생성자를 만든 것이다.
     */
    protected Board(User writer,List<Comment> comments,PostDetails postDetails){
        this.writer = writer;
        this.comments = comments;
        this.postDetails = postDetails != null ? postDetails : PostDetails.builder().build();
    }
    public void update(String title,String content,String file){
        if (this.postDetails == null){
            this.postDetails = PostDetails.builder().build();
        }
        this.postDetails.update(title,content,file);
    }
    /**
     * -- SETTER --
     *  외래 키 관리로 각각의 board 는 해당 게시물 내용이 DB 에서는 보여지지 않는다.
     *  처음에는 각각의 board 에서도 필드값들에 대한 내용들이 보여져야 하지 않을까? 라는 생각을 했지만
     *  값들이 postDetails 에도 있고 board 에도 있게 되면 중복으로 저장되는 것이므로 비효율적이다.
     *  그래서 어느 board 에 저장할지는 Id 값으로 찾아준다.
     */



}
