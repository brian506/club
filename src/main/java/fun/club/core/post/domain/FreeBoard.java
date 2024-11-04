package fun.club.core.post.domain;


import fun.club.core.comment.domain.Comment;
import fun.club.core.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@DiscriminatorValue("freeboard") // dtype 으로 조회하는 것
@Entity
@Getter
@NoArgsConstructor
public class FreeBoard extends Board{

    @Builder
    public FreeBoard(User writer,List<Comment> comments,PostDetails postDetails) {
        super(writer,comments,postDetails);
    }
}
