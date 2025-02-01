package fun.club.core.comment.repository;

import fun.club.core.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // boardId 로 댓글 찾기
    @Query("SELECT c FROM Comment c WHERE c.board.id = :boardId ORDER BY c.createdDate ASC")
    Page<Comment> findByPostIdOOrderByCreatedDate(@Param("boardId") Long boardId, Pageable pageable);


}
