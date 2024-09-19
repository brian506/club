package fun.club.api.comment;

import fun.club.common.request.CommentRequest;
import fun.club.common.util.SuccessResponse;
import fun.club.service.comment.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static fun.club.api.post.ApiPostUrlConstants.COMMENTS_ADD_POST;
import static fun.club.api.post.ApiPostUrlConstants.COMMENTS_DELETE_POST;
import static fun.club.common.util.SuccessMessages.POST_CREATE_SUCCESS;
import static fun.club.common.util.SuccessMessages.POST_DELETE_SUCCESS;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(COMMENTS_ADD_POST)
    public ResponseEntity addComment(@RequestBody @Valid CommentRequest request) throws IOException {
        Long comment = commentService.create(request);
        SuccessResponse response = new SuccessResponse(true,POST_CREATE_SUCCESS,request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(COMMENTS_DELETE_POST)
    public ResponseEntity deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        SuccessResponse response = new SuccessResponse<>(true,POST_DELETE_SUCCESS,commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
