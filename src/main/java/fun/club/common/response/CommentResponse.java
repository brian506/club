package fun.club.common.response;

import fun.club.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponse {

    private Long id;

    private String comment;

    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    private String username;
}
/**
 * 사용자에게 댓글 작성 시간도 보여주기 위해서는 time 필드값 필요
 */
