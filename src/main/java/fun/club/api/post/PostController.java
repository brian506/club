package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.util.SuccessResponse;
import fun.club.core.post.domain.Post;
import fun.club.service.post.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                        @RequestPart MultipartFile image) throws IOException {
        Long post = postService.createPost(dto,image);
        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
/**
 * @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션입니다.
 * MultipartFile이 포함되어 있는 경우 MultipartResolver가 동작하여 역직렬화를 하게 됩니다.
 * 만약 MultipartFile이 포함되어있지 않다면, @RequestBody와 마찬가지로 동작하게 됩니다.
 */