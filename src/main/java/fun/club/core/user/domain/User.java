package fun.club.core.user.domain;

import fun.club.core.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

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
    private Role role;

    private String refreshToken;
    // admin과 같은 리프레시 토큰을 받도록 하자
    // 어차피 작은 프로젝트라 괜춘

    public void passwordEncode(String encodePassword){
        this.password = encodePassword;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

}