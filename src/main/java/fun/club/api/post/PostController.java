package fun.club.api.post;


//import java.io.IOException;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/postDetails")
//public class PostController {
//
//    private final PostService postService;
//
//    @PostMapping("/create")
//    public ResponseEntity<?> addPosts(@ModelAttribute @Valid PostCreateDto dto,
//                                        @RequestPart MultipartFile image) throws IOException {
//        Long postDetails = postService.createPost(dto,image);
//        SuccessResponse response = new SuccessResponse(true,"게시물 생성 완료",postDetails);
//        return  new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @PostMapping("/update")
//    public ResponseEntity<?> updatePosts(@ModelAttribute @Valid PostUpdateDto dto,
//                                         @RequestPart MultipartFile image) throws IOException {
//        Long postDetails = postService.updatePost(dto, dto.getPostId(),image);
//        SuccessResponse response = new SuccessResponse(true,"게시물 수정 완료",postDetails);
//        return  new ResponseEntity<>(response, HttpStatus.OK);
//    }
//
//    @GetMapping("/users/{userId}")
//    public ResponseEntity<?> getPostsByUserId(@PathVariable("userId") Long userId,
//        @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
//
//        Page<PostDetails> postDetails = postService.findAllByWriter(pageable,userId);
//        SuccessResponse response = new SuccessResponse(true,"사용자의 게시물 조회 완료",postDetails);
//        return  new ResponseEntity<>(response, HttpStatus.OK);
//    } // 5개씩 내림차순으로 정렬
//
//}
/**
 * @RequestPart는 HTTP request body에 multipart/form-data 가 포함되어 있는 경우에 사용하는 어노테이션입니다.
 * MultipartFile이 포함되어 있는 경우 MultipartResolver가 동작하여 역직렬화를 하게 됩니다.
 * 만약 MultipartFile이 포함되어있지 않다면, @RequestBody와 마찬가지로 동작하게 됩니다.
 */