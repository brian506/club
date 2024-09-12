package fun.club.core.admin.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import fun.club.common.base.BaseTimeEntity;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminUser extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "admin_user_id")
    private Long id;

    private String username;

    private String password;

    private String email;

    private int phoneNumber;

    private String profileImageUrl;

    private String refreshToken;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    @Setter
//    @JsonBackReference
//    private User user;

    @Setter
    private LocalDateTime assignedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}