package fun.club.api.post;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.response.BoardResponse;
import fun.club.common.util.SuccessResponse;
import fun.club.service.post.FreeBoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class FreeBoardController {

    private final FreeBoardService freeBoardService;

    // 게시물 작성
    @PostMapping(FREE_BOARDS_ADD_POST)
    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
                                      @RequestPart MultipartFile image) throws IOException {
        Long post = freeBoardService.create(dto,image);
        SuccessResponse response = new SuccessResponse(true,POST_CREATE_SUCCESS,post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 게시물 전체 수정
    @PutMapping(FREE_BOARDS_UPDATE_POST)
    @PreAuthorize("isAuthenticated()") // 작성자만 수정 가능
    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
                                         @PathVariable Long boardId,
                                         @RequestPart MultipartFile image) throws IOException {
        Long post = freeBoardService.update(dto, boardId, image);
        SuccessResponse response = new SuccessResponse(true,POST_UPDATE_SUCCESS,post);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시물 삭제
    @DeleteMapping(FREE_BOARDS_DELETE_POST)
    @PreAuthorize("isAuthenticated()") // 작성자만 수정 가능
    public ResponseEntity<?>  deletePosts(@PathVariable Long boardId){
        freeBoardService.delete(boardId);
        SuccessResponse response = new SuccessResponse<>(true,POST_DELETE_SUCCESS,boardId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 게시물 단일 조회
    @GetMapping(FREE_BOARDS_FIND_BY_ID)
    public ResponseEntity<?> findById(@PathVariable Long boardId){
        BoardResponse board = freeBoardService.findById(boardId);
        SuccessResponse response = new SuccessResponse(true,ID_POSTS_RETRIEVE_SUCCESS,board);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 작성자 이름으로 게시물 조회
    @GetMapping(FREE_BOARDS_FIND_BY_USER)
    public ResponseEntity<?> findAllByWriter(@PathVariable("userId") Long userId,
                                             @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponse> boards = freeBoardService.findAllByWriter(userId,pageable);
        SuccessResponse response = new SuccessResponse(true,USER_POSTS_RETRIEVE_SUCCESS,boards);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    } // 5개씩 내림차순으로 정렬

    // 제목으로 게시물 조회
    @GetMapping(FREE_BOARDS_FIND_BY_TITLE)
    public ResponseEntity<?> findByTitle(@RequestParam String title){
        List<BoardResponse> boards = freeBoardService.findByTitle(title);
        SuccessResponse response = new SuccessResponse(true,TITLE_POSTS_RETRIEVE_SUCCESS,boards);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 게시물 전체 조회
    @GetMapping(FREE_BOARDS_FIND_ALL)
    public ResponseEntity<?> findAll(@PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<BoardResponse> boards = freeBoardService.findAllFromBoard(pageable);
        SuccessResponse response = new SuccessResponse(true,ALL_POSTS_RETRIEVE_SUCCESS,boards);
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
