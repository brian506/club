package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.response.BoardResponse;
import fun.club.common.util.SuccessResponse;
import fun.club.core.post.domain.Board;
import fun.club.service.post.NoticeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static fun.club.api.post.ApiPostUrlConstants.*;
import static fun.club.common.util.SuccessMessages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class NoticeBoardController {

    private final NoticeBoardService noticeBoardService;

    // 게시물 작성
    @PostMapping(NOTICE_BOARDS_ADD_POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                      @RequestPart MultipartFile image) throws IOException {
        Long post = noticeBoardService.create(dto,image);
        SuccessResponse response = new SuccessResponse(true,POST_CREATE_SUCCESS,post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시물 수정
    @PutMapping(NOTICE_BOARDS_UPDATE_POST)
    @PreAuthorize("isAuthenticated()") // 인증성공한 유저만 가능
    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
                                         @PathVariable Long boardId,
                                         @RequestPart MultipartFile image) throws IOException {
        Long post = noticeBoardService.update(dto,boardId, image);
        SuccessResponse response = new SuccessResponse(true,POST_UPDATE_SUCCESS,post);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시물 삭제
    @DeleteMapping(NOTICE_BOARDS_DELETE_POST)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?>  delete(@PathVariable Long boardId){
        noticeBoardService.delete(boardId);
        SuccessResponse response = new SuccessResponse<>(true,POST_DELETE_SUCCESS,boardId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 사용자 이름으로 게시물 조회
    @GetMapping(NOTICE_BOARDS_FIND_BY_USER)
    public ResponseEntity<?> findAllByWriter(@PathVariable("userId") Long userId,
        @PageableDefault(size = 5, direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponse> boards = noticeBoardService.findAllByWriter(userId,pageable);
        SuccessResponse response = new SuccessResponse(true,USER_POSTS_RETRIEVE_SUCCESS,boards);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    } // 5개씩 내림차순으로 정렬
    // 이렇게 BoardResponse 에 User 객체를 넣으면 User 에 대한 모든 정보가 나온다. User 의 이름만 나오도록 수정

    // 제목으로 게시물 조회
    @GetMapping(NOTICE_BOARDS_FIND_BY_TITLE)
    public ResponseEntity<?> findByTitle(@RequestParam String title){
        List<BoardResponse> boards = noticeBoardService.findByTitle(title);
        SuccessResponse response = new SuccessResponse(true,TITLE_POSTS_RETRIEVE_SUCCESS,boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 전체 조회
    @GetMapping(NOTICE_BOARDS_FIND_ALL)
    public ResponseEntity<?> findAll(
            @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
            @RequestParam(required = false, defaultValue = "5", value = "size") int pageSize,
            @RequestParam(required = false, defaultValue = "createdAt", value = "criteria") String criteria) {
        Page<BoardResponse> boards = noticeBoardService.findAllFromBoard(pageNo,pageSize,criteria);
        SuccessResponse response = new SuccessResponse(true, ALL_POSTS_RETRIEVE_SUCCESS, boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
/**
 * @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션입니다.
 * MultipartFile이 포함되어 있는 경우 MultipartResolver가 동작하여 역직렬화를 하게 됩니다.
 * 만약 MultipartFile이 포함되어있지 않다면, @RequestBody와 마찬가지로 동작하게 됩니다.
 *
 * @PreAuthorize("isAuthenticated()") : 로그인한 사용자(권한 인증 받은)만 수행가능
 */

/**
 * ResponseEntity<?> 응답 객체
 * 상태 코드,헤더 정보 포함
 * -> RESTful API 표준을 따르는 경우, 상태 코드에 따라 응답을 다르게 처리하는 경우
 *
 * SuccessResponseDto 응답 객체
 * 클라이언트가 일관된 형식의 데이터(JSON)만을 기대하는 경우
 *
 */