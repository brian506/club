package fun.club.core.admin.domain;

import fun.club.common.base.BaseTimeEntity;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToOne
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    @Setter
    private LocalDateTime assignedAt;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}