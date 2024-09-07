package fun.club.service.post;

import fun.club.common.mapper.PostMapper;
import fun.club.common.request.PostCreateDto;
import fun.club.common.util.SecurityUtil;
import fun.club.core.board.domain.FreeBoard;
import fun.club.core.board.domain.NoticeBoard;
import fun.club.core.board.repository.FreeBoardRepository;
import fun.club.core.board.repository.NoticeBoardRepository;
import fun.club.core.post.domain.Post;
import fun.club.core.post.repository.PostRepository;
import fun.club.core.user.domain.Role;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FreeBoardRepository freeBoardRepository;
    private final NoticeBoardRepository noticeBoardRepository;
    private final PostMapper postMapper;


    // 게시물 작성
    public Long createPost(PostCreateDto postDto, MultipartFile file) throws IOException {

        Post post = postMapper.toEntity(postDto);

        User writer = userRepository.findByEmail(SecurityUtil.getLoginUsername())
                // findByEmail 로 해야 사용자를 찾을 수 있다.  jwt에서 email 로 찾는 걸로 정했다.
                .orElseThrow(() -> new UsernameNotFoundException("로그인된 사용자 정보를 찾을 수 없습니다."));

        // 게시물의 작성자 설정
        post.setWriter(writer);

        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            post.setImage(fileName);
        }

        addBoardType(post, postDto.getBoardType());
        postRepository.save(post);

        return post.getId();
    }
    // 로그인까지는 됐는데 아직 어떤 user 가 post 를 작성했는 지 구현이 안됨,
    // createPost 에서 user 도 요청을 받아야 하나? 그래야 DB 에 user_id 가 뜸?

    @Transactional(readOnly = true)
    public void addBoardType(Post post, String boardType) {
        if ("freeboard".equalsIgnoreCase(boardType)) {

            FreeBoard freeBoard = freeBoardRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("자유게시판이 존재하지 않습니다."));
            freeBoard.addPost(post);
            post.setFreeBoard(freeBoard);
        } else if ("noticeboard".equalsIgnoreCase(boardType)) {
            User user = userRepository.findByUsername(SecurityUtil.getLoginUsername())
                    .orElseThrow(() -> new UsernameNotFoundException("로그인된 사용자 정보를 찾을 수 없습니다."));
            if (!user.getRole().equals(Role.ADMIN)) {
                throw new IllegalArgumentException("공지게시판은 ADMIN만 작성할 수 있습니다.");
            }
            NoticeBoard noticeBoard = noticeBoardRepository.findById(1L)
                    .orElseThrow(() -> new IllegalArgumentException("공지게시판이 존재하지 않습니다."));
            noticeBoard.addPost(post); // Post를 NoticeBoard에 추가
        }
    }



    // 게시물 수정
//    public Long updatePost(PostUpdateDto postDto, Long postId) {
//
//    }

    // 게시물 삭제

    // 게시물 조회(pageable)

    //
}

