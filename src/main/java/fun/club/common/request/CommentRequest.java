package fun.club.common.request;

import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Board;
import fun.club.core.user.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequest {

    @NotBlank
    @Size(min = 1, max =50,message = "글자 수 50 제한")
    private String comment;

}

/**
 * 댓글을 새로 생성할 때는 처음에 commentId 가 없으므로 commentId는 요청보내지 않는다
 */
//    private User writer;
// 작성자는 로그인한 유저가 댓글을 작성하는 것이기 때문에 dto 에 넣지 않았다
/**
 * boardId 는 컨트롤러에서 URL 에 경로 변수로 전달하면 DTO 에서는 필드값으로 필요하지 않다.
 */
