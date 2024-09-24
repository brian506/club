package fun.club.core.post.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@DiscriminatorValue("freeboard") // dtype 으로 조회하는 것
@Entity
@Getter
@NoArgsConstructor
public class FreeBoard extends Board{



}
