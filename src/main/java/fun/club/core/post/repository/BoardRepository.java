package fun.club.core.post.repository;

import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.post.domain.PostDetails;


import fun.club.core.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



import java.util.List;


@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByWriter(User writer, Pageable pageable);
    // userId 는 board 에 없으므로 User 객체를 가지고 와서 하야함
    // findPostsByWriter 와 같이 정확히 파라미터 지칭하는 값을 설정해야 함. board 에는 writer 로 매핑되어 있는데 user 로 하면 매핑 안됨

    @Query("SELECT p FROM Board p WHERE TYPE(p) = NoticeBoard AND p.postDetails.title = :title")
    List<NoticeBoard> findByTitleInNoticeBoard(@Param("title") String title);

    @Query("SELECT p FROM Board p WHERE TYPE(p) = FreeBoard AND p.postDetails.title = :title")
    List<FreeBoard> findByTitleInFreeBoard(@Param("title") String title);

    @Query("SELECT p FROM Board p WHERE TYPE(p) = FreeBoard ")
    Page<FreeBoard> findAllFreeBoardPosts(Pageable pageable);

    @Query("SELECT p FROM Board p WHERE TYPE(p) = NoticeBoard")
    Page<NoticeBoard> findAllNoticeBoardPosts(Pageable pageable);



}
