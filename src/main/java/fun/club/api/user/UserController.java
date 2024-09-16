package fun.club.api.user;

import fun.club.common.request.LoginDto;
import fun.club.common.request.SignupRequestDto;
import fun.club.common.response.AuthResponse;
import fun.club.common.response.UserInfoResponse;
import fun.club.common.util.SuccessResponse;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.service.JwtService;
import fun.club.secure.service.LoginService;
import fun.club.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping()
public class UserController {

    private final UserService userService;
    private final LoginService loginService;


    // 회원가입
    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto dto) throws Exception {
        Long user = userService.signUp(dto);
        SuccessResponse response = new SuccessResponse<>(true,"회원 등록 성공",user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        AuthResponse authResponse = (AuthResponse) loginService.loadUserByUsername(loginDto.getEmail());

        SuccessResponse<AuthResponse> response = new SuccessResponse<>(true, "로그인 성공", authResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
    // 프로필 사진 업로드
    @PostMapping("/users/{userId}/profile")
    public ResponseEntity<?> uploadProfile(@PathVariable Long userId,
                                                @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        userService.updateProfile(userId, profileImage);

        String profileImageUrl = userService.getProfileImageUrl(userId);
        SuccessResponse response = new SuccessResponse<>(true,"프로필 사진 업로드 성공",profileImageUrl);

        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    // 본인 프로필 조회
    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<?> getUserId(@PathVariable Long userId){
        UserInfoResponse id = userService.findById(userId);
        SuccessResponse response = new SuccessResponse<>(true,"회원 조회 성공",id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 다른 사용자가 프로필 조회 - 이름
    @GetMapping("/users/usernames")
    public ResponseEntity<?> getUserNames(@RequestParam String username) {
        UserInfoResponse user = userService.findByUsername(username);
        SuccessResponse response = new SuccessResponse<>(true,"회원이름으로 조회 성공",user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }


     /** 인증된(로그인중인) 사용자 조회
      *  현재 로그인한 사용자의 정보를 가져오는 동작을 해야 회원 본인 프로필을 조회가 가능하다.
      */
    @GetMapping("/users/userInfo")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserInfoResponse> findLoginUser() {
        // 현재 인증된 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfoResponse userInfo = userService.findByEmail(email);
        SuccessResponse response = new SuccessResponse(true,"인증정보 조회 성공",userInfo);

        return ResponseEntity.ok(userInfo);

    }
}


