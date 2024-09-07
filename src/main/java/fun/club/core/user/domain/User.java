package fun.club.core.user.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import fun.club.common.base.BaseTimeEntity;
import fun.club.core.admin.domain.AdminUser;
import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String email;

    private String password;

    private int phoneNumber;

    private String birth;

    private String personality; // mbti

    @Setter
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;

    private int absencePoint; // 불참시 포인트 +1

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonManagedReference
    private AdminUser admin;

    // 회원탈퇴 -> 작성한 게시글,댓글 모두 삭제
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Post> posts;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    private String refreshToken;
    // admin과 같은 리프레시 토큰을 받도록 하자
    // 어차피 작은 프로젝트라 괜춘

    public void passwordEncode(String encodePassword) {
        this.password = encodePassword;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}


/** post 블로그에서 작성하자
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
 * post 가 주인 클래스일 떄, post 를 조회할 때 user 정보가 필요한 경우에만 DB에서 가져오도록 post 클래스에서 설정하는 것이 성능 최적화에 좋다.
 * 그래서 보통 ~XToOne 일 때 Fetch 를 설정한다.
 */
