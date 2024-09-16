package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.util.SuccessResponse;
import fun.club.service.post.NoticeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;

    @PostMapping("/noticeBoard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                      @RequestPart MultipartFile image,
                                        Authentication authentication) throws IOException {
        if (authentication != null) {
            authentication.getAuthorities().forEach(auth ->
                    System.out.println("User Authority: " + auth.getAuthority()));
        } else {
            System.out.println("No authentication found");
        }

        Long post = noticeBoardService.create(dto,image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/noticeBoard")
    @PreAuthorize("hasRole('ADMIN')") // 작성자만 수정 가능
    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
                                         @RequestPart MultipartFile image) throws IOException {
        Long post = noticeBoardService.update(dto, dto.getPostId(), image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/noticeBoard/{postId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>  deletePosts(@PathVariable Long postId){
        noticeBoardService.delete(postId);
        SuccessResponse response = new SuccessResponse<>(true,"게시물 삭제 완료",postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
/**
 * @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션입니다.
 * MultipartFile이 포함되어 있는 경우 MultipartResolver가 동작하여 역직렬화를 하게 됩니다.
 * 만약 MultipartFile이 포함되어있지 않다면, @RequestBody와 마찬가지로 동작하게 됩니다.
 *
 * @PreAuthorize("isAuthenticated()") : 로그인한 사용자(권한 인증 받은)만 수행가능
 */