package fun.club.common.response;

import fun.club.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponse {

    private Long commentId;

    private String comment;

    private Long boardId;

    private User writer;
}
