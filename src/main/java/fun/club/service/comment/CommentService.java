package fun.club.service.comment;

import fun.club.common.mapper.CommentMapper;
import fun.club.common.request.CommentRequest;
import fun.club.common.util.OptionalUtil;
import fun.club.common.util.SecurityUtil;
import fun.club.core.comment.domain.Comment;
import fun.club.core.comment.repository.CommentRepository;
import fun.club.core.post.domain.Board;
import fun.club.core.post.repository.BoardRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    // 댓글 생성
    public Long create(CommentRequest commentRequest) throws IOException {

        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()), "존재하지 않는 회원입니다.");

        Board board = OptionalUtil.getOrElseThrow(boardRepository.findById(commentRequest.getBoardId()),"존재하지 않는 게시물입니다.");

        Comment comment = commentRequest.toEntity(writer,board);

        return commentRepository.save(comment).getId();

    }
    // 댓글 삭제
    public void delete(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        commentRepository.delete(comment.get());
    }






}
/**
 * 댓글만 조회하는 기능은 만들지 않고 게시물을 조회했을 때 댓글도 같이 보는 걸로 할거임
 */

/**
 * @Transactional 은 메서드 내에서 수행되는 데이터베이스 작업이 하나로 묶인다.
 * 메서드 내에서 수행되는 로직이 하나라도 오류가 있으면 롤백되므로 부분적으로 업데이트 되는 상황을 방지한다.
 */