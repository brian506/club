package fun.club.api.comment;

import fun.club.common.request.CommentRequest;
import fun.club.common.response.CommentResponse;
import fun.club.common.util.SuccessResponse;

import fun.club.service.comment.CommentService;
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

import java.io.IOException;

import static fun.club.api.post.ApiPostUrlConstants.*;
import static fun.club.common.util.SuccessMessages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;


    @PostMapping(COMMENTS_ADD_POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addComment(@PathVariable Long boardId,
                                        @RequestBody @Valid CommentRequest request) throws IOException {
        Long comment = commentService.create(request,boardId);
        SuccessResponse response = new SuccessResponse(true,POST_CREATE_SUCCESS,comment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PutMapping(COMMENTS_UPDATE_POST)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateComment(@PathVariable Long commentId,
                                           @RequestBody @Valid CommentRequest request) throws IOException {
        commentService.update(request,commentId);
        SuccessResponse response = new SuccessResponse(true,POST_UPDATE_SUCCESS,commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping(COMMENTS_GET_COUNT)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCommentCount(@PathVariable Long boardId){
        Long count = commentService.getCommentCount(boardId);
        SuccessResponse response = new SuccessResponse(true,"댓글 조회 성공",count);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<?> getComments(@PathVariable Long boardId,
                                                             @PageableDefault(size = 5,sort = "createdDate",direction = Sort.Direction.ASC) Pageable pageable) throws IOException {
        Page<CommentResponse> comments = commentService.getAllComment(boardId,pageable);
        SuccessResponse response = new SuccessResponse(true,"댓글 조회 성공",comments);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @DeleteMapping(COMMENTS_DELETE_POST)
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId,@PathVariable Long boardId) throws IOException {
        commentService.delete(commentId,boardId);
        SuccessResponse response = new SuccessResponse<>(true,POST_DELETE_SUCCESS,commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
