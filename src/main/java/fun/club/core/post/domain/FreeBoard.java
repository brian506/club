package fun.club.core.post.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@DiscriminatorValue("freeboard") // dtype 으로 조회하는 것
@Entity
@Getter
@NoArgsConstructor
@SuperBuilder
public class FreeBoard extends Board{

    @Embedded
    private PostDetails postDetails;
}
