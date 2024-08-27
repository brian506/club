package fun.club.core.base;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {

    //생성일시
    @Column(name = "created_date", updatable = false) // @CreatedDate로도 바로 할 수 있음
    private LocalDateTime createdDate;

    //최종 수정일시
    @Column(name = "modified_date") // @LastModifiedDate로도 바로 할 수 있음
    private LocalDateTime modifiedDate;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간 반환
        createdDate = now;
        modifiedDate = now;
    }

    @PreUpdate
    public void preUpdate() { // 엔티티가 db에서 엡데이트될 때마다 최신 시간으로 갱신
        modifiedDate = LocalDateTime.now();
    }


    @CreatedBy // 등록자
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy // 수정자
    private String modifiedBy;
}