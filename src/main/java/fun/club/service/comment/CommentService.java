package fun.club.service.comment;

import fun.club.common.mapper.CommentMapper;
import fun.club.common.request.CommentRequest;
import fun.club.common.response.CommentResponse;
import fun.club.common.util.OptionalUtil;
import fun.club.common.util.SecurityUtil;
import fun.club.core.comment.domain.Comment;
import fun.club.core.comment.repository.CommentRepository;
import fun.club.core.post.domain.Board;
import fun.club.core.post.repository.BoardRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final CommentMapper commentMapper;


    // 댓글 생성
    @Transactional
    public Long create(CommentRequest commentRequest,Long boardId) throws IOException {

        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()), "존재하지 않는 회원입니다.");

        Board board = OptionalUtil.getOrElseThrow(boardRepository.findById(boardId),"존재하지 않는 게시물입니다.");

        Comment comment = commentMapper.toEntity(commentRequest,writer,board);

        commentRepository.save(comment);
        board.incrementCommentCount();

        return comment.getId();

    }
    // 댓글 업데이트
    public void update(CommentRequest commentRequest,Long commentId) throws IOException {
        Comment comment = OptionalUtil.getOrElseThrow(commentRepository.findById(commentId),"존재하지 않는 댓글입니다");
        comment.update(
                commentRequest.getComment()
        );
        comment.getId();
    }

    @Transactional(readOnly = true)
    public Long getCommentCount(Long boardId) {
        return boardRepository.findById(boardId)
                .map(Board::getCommentCount)
                .orElse(0L); // boardId 가 null 일 경우

    }

    // 특정 게시물의 전체 댓글 시간순 조회
    public Page<CommentResponse> getAllComment(Long boardId,Pageable pageable) throws IOException {
        Page<Comment> comments = commentRepository.findByPostIdOOrderByCreatedDate(boardId,pageable);
        return comments.map(commentMapper::commentToDto);
    }

    // 댓글 삭제
    public void delete(Long commentId,Long boardId) throws IOException {
        Comment comment = OptionalUtil.getOrElseThrow(commentRepository.findById(commentId),"존재하지 않는 댓글입니다");
        if(comment.getBoard().getId().equals(boardId)){
            commentRepository.delete(comment);
        }
        commentRepository.delete(comment);
        Board board = comment.getBoard();
        board.decrementCommentCount();

    }






}
/**
 * 댓글만 조회하는 기능은 만들지 않고 게시물을 조회했을 때 댓글도 같이 보는 걸로 할거임
 */

/**
 * @Transactional 은 메서드 내에서 수행되는 데이터베이스 작업이 하나로 묶인다.
 * 메서드 내에서 수행되는 로직이 하나라도 오류가 있으면 롤백되므로 부분적으로 업데이트 되는 상황을 방지한다.
 */