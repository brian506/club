package fun.club.alarm.domain;

import fun.club.common.base.BaseTimeEntity;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Notification extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content; // 알림의 내용

    private String url; // 관련 링크

    @Column(nullable = false)
    private Boolean isRead; // 읽었는지의 여부

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType; // 알림 종류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE) // DB 에서의 삭제를 담당한다.
    private User receiver;




}
