package fun.club.core.board.domain;


import fun.club.core.post.domain.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Table(name = "FREEBOARD")
@Entity
@Getter
@RequiredArgsConstructor
public class FreeBoard {

    @Id @GeneratedValue
    @Column(name = "freeboard_id")
    private Long id;

    @OneToMany(mappedBy = "freeboard", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();



}
