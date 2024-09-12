package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.util.SuccessResponse;
import fun.club.service.post.NoticeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/noticeBoard")
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                      @RequestPart MultipartFile image) throws IOException {
        Long post = noticeBoardService.create(dto,image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("@securityUtil.getLoginUsername() == #postUpdateDto.writerEmail") // 작성자만 수정 가능
    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
                                         @RequestPart MultipartFile image) throws IOException {
        Long post = noticeBoardService.update(dto, dto.getPostId(), image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
/**
 * @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션입니다.
 * MultipartFile이 포함되어 있는 경우 MultipartResolver가 동작하여 역직렬화를 하게 됩니다.
 * 만약 MultipartFile이 포함되어있지 않다면, @RequestBody와 마찬가지로 동작하게 됩니다.
 */