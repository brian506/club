package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.util.SuccessResponse;
import fun.club.service.post.FreeBoardService;
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
@RequestMapping("/freeBoard")
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    @PostMapping()
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                      @RequestPart MultipartFile image) throws IOException {
        Long post = freeBoardService.create(dto,image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping
    @PreAuthorize("@securityUtil.getLoginUsername() == #postUpdateDto.writerEmail")
    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
                                         @RequestPart MultipartFile image) throws IOException {
        Long post = freeBoardService.update(dto, dto.getPostId(), image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
