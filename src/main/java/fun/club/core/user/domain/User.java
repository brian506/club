package fun.club.core.user.domain;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import fun.club.common.base.BaseTimeEntity;
import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.PostDetails;
import jakarta.persistence.*;
import lombok.*;

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

    @Setter
    private int absencePoint; // 불참시 포인트 +1

//    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
//    @JsonManagedReference
//    private AdminUser admin; // User 의 정보를 가져오기 위함이라면 굳이 매핑을 하지 않고 빌더를 통해서 해당 엔티티에서 필요한 필드값들을 불러와서 쓸 수 있다.

    // 회원탈퇴 -> 작성한 게시글,댓글 모두 삭제
    @OneToMany(mappedBy = "writer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Board> boards;

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


