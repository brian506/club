package fun.club.common.request;

import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Board;
import fun.club.core.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentRequest {

    @NotBlank
    @Size(min = 1, max =50)
    private String comment;

    private Long boardId;

    public Comment toEntity(User writer, Board board) {
        return Comment.builder()
                .comment(comment)
                .writer(writer)
                .board(board)
                .build();
    }
}

/**
 * 댓글을 새로 생성할 때는 처음에 commentId 가 없으므로 commentId는 요청보내지 않는다
 */
//    private User writer;
// 작성자는 로그인한 유저가 댓글을 작성하는 것이기 때문에 dto 에 넣지 않았다
