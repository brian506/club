package fun.club.service.post;

import fun.club.common.mapper.BoardMapper;
import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.util.OptionalUtil;
import fun.club.common.util.SecurityUtil;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.post.repository.BoardRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class FreeBoardService implements PostService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    // 게시물 작성
    @Override
    public Long create(PostCreateDto postCreateDto, MultipartFile image) throws IOException {

        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()),"존재하지 않는 회원입니다.");

        FreeBoard freeBoard = boardMapper.freeBoardFromDto(postCreateDto,writer);

        if (image != null && !image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            freeBoard.getPostDetails().setFile(fileName);
        }

        return boardRepository.save(freeBoard).getId();
    }

    //게시물 수정
    @Override
    public Long update(PostUpdateDto postUpdateDto, Long postId, MultipartFile image) throws IOException {
        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()),"존재하지 않는 회원입니다.");

        NoticeBoard noticeBoard= (NoticeBoard) OptionalUtil.getOrElseThrow(boardRepository.findById(postId),"존재하지 않는 게시물입니다.");

//        if (!noticeBoard.getWriter().getId().equals(writer.getId())) {
//            throw new AccessDeniedException("해당 게시물에 접근할 수 없습니다.");
//        }// 컨트롤러에서 @preAuthorize 이용
        boardMapper.updateBoardFromDto(postUpdateDto,noticeBoard);// set 을 이용하지 않고 mapper 를 이용해서 엔티티를 업데이트한다.

        if (image != null && !image.isEmpty()) {
            String fileName = image.getOriginalFilename();
            noticeBoard.getPostDetails().setFile(fileName);// dto 관련된 것들만 주로 mapper 를 이용하는 편이 좋다. 이미지 파일의 존재 유무에 대한 것이므로 그냥 setter 로 설정
        }

        return noticeBoard.getId(); // 게시물을 수정할 때는 repository 에 저장하지 않아도 새로운 값으로 저장된다. 처음에 create 할 때는 리포지토리 필요
    }

    // 게시물 삭제
    @Override
    public void delete(Long postId) {
        Optional<Board> noticeBoard = boardRepository.findById(postId);
        boardRepository.delete(noticeBoard.get());
    }

    // 게시물 조회(pageable) - 사용자가 작성한 게시물들 조회
    @Override
    public Page<Board> findAllByWriter(Pageable pageable, Long writerId) {
        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()),"존재하지 않는 회원입니다.");
        return boardRepository.findPostsByWriter(writer,pageable);
    }
}
