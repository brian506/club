package fun.club.service.board;

import fun.club.common.mapper.PostMapper;
import fun.club.common.request.PostCreateDto;
import fun.club.common.util.SecurityUtil;
import fun.club.core.board.repository.FreeBoardRepository;
import fun.club.core.post.domain.Post;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardService {




    // 게시물 수정
//    public Long updatePost(PostUpdateDto postDto, Long postId) {
//
//    }

    // 게시물 삭제

    // 게시물 조회(pageable)

    //
}


