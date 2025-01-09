package fun.club.service.post;

import fun.club.common.mapper.BoardMapper;
import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.response.BoardResponse;
import fun.club.common.util.ListUtil;
import fun.club.common.util.OptionalUtil;
import fun.club.common.util.SecurityUtil;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.post.repository.BoardRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.service.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional()
@RequiredArgsConstructor
public class FreeBoardService implements PostService{

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final FileService fileService;

    // 게시물 작성
    @Override
    public Long create(PostCreateDto postCreateDto) throws IOException {

        User writer = OptionalUtil.getOrElseThrow(userRepository.findByEmail(SecurityUtil.getLoginUsername()),"존재하지 않는 회원입니다.");
        FreeBoard freeBoard = boardMapper.freeBoardFromDto(postCreateDto,writer);
        MultipartFile file = postCreateDto.getImage();

        if (file != null && !file.isEmpty()) {
            String fileName = fileService.savePostFile(file);
            freeBoard.getPostDetails().setFile(fileName);
        }

        return boardRepository.save(freeBoard).getId();
    }

    //게시물 수정
    @Override
    public Long update(PostUpdateDto postUpdateDto, Long boardId) throws IOException {
        FreeBoard freeBoard = (FreeBoard) OptionalUtil.getOrElseThrow(
                boardRepository.findById(boardId),"존재하지 않는 게시물입니다.");

        // 연관관계 메서드로 update 기능 수행
        freeBoard.update(
                postUpdateDto.getTitle(),
                postUpdateDto.getContent(),
                null
        );
        MultipartFile file = postUpdateDto.getFile();
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            freeBoard.getPostDetails().setFile(fileName);// dto 관련된 것들만 주로 mapper 를 이용하는 편이 좋다. 이미지 파일의 존재 유무에 대한 것이므로 그냥 setter 로 설정
        }
        return freeBoard.getId(); // dirty checking
    } // 파라미터로 file 을 받지 않고 dto 에 있는 값을 써준다

    // 게시물 삭제
    @Override
    public void delete(Long postId) {
        Optional<Board> freeBoard = boardRepository.findById(postId);
        boardRepository.delete(freeBoard.get());
    }

    // 단일 게시물 조회
    public BoardResponse findById(Long postId) {
        FreeBoard board = (FreeBoard) OptionalUtil.getOrElseThrow(boardRepository.findById(postId),"존재하지 않는 게시물입니다.");
        board.setViews(board.getViews() + 1); // 조회수 증가
        boardRepository.save(board);
        return boardMapper.responseToDto(board);
    }

    // 게시물 조회(pageable) - 사용자가 작성한 게시물들 조회
    @Override
    public Page<BoardResponse> findAllByWriter(Long userId,Pageable pageable) {
        User writer = OptionalUtil.getOrElseThrow(userRepository.findById(userId),"존재하지 않는 회원입니다.");
        Page<Board> boards = boardRepository.findAllByWriter(writer,pageable);
        return boards.map(boardMapper::responseToDto);
    }

    @Override
    public List<BoardResponse> findByTitle(String title) {

        List<FreeBoard> boards = ListUtil.getOrElseThrowList(boardRepository.findByTitleInFreeBoard(title),"존재하지 않는 게시물입니다.");
        // 같은 제목이 여러 개 있을 수 있으니 List로 받음

        // List<FreeBoard> -> List<BoardResponse> 변환
        return boards.stream()
                .map(boardMapper::responseToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BoardResponse> findAllFromBoard(int pageNo, int pageSize, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, criteria));
        Page<FreeBoard> boards = boardRepository.findAllFreeBoardPosts(pageable);
        return boards.map(boardMapper::responseToDto); // FreeBoard를 BoardResponse로 변환
    }
}
