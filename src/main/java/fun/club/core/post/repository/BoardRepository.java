package fun.club.core.post.repository;

import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.PostDetails;


import fun.club.core.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findPostsByWriter(User writer, Pageable pageable);
    // userId 는 PostDetails 에 없으므로 User 객체를 가지고 와서 하야함
    // findPostsByWriter 와 같이 정확히 파라미터 지칭하는 값을 설정해야 함. postDetails 에는 writer 로 매핑되어 있는데 user 로 하면 매핑 안됨

    Optional<Board> findByTitle(String title);

    @Query("SELECT p FROM Board p WHERE TYPE(p) = FreeBoard ")
    List<Board> findAllFreeBoardPosts();

    @Query("SELECT p FROM Board p WHERE TYPE(p) = NoticeBoard")
    List<Board> findAllNoticeBoardPosts();



}
