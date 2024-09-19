package fun.club.common.response;

import fun.club.core.user.domain.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private User writer;
    private List<CommentResponse> commentResponses;

}
