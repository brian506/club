package fun.club.api.user;

import fun.club.common.request.LoginDto;
import fun.club.common.request.SignupRequestDto;
import fun.club.common.request.UserUpdateDto;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static fun.club.api.post.ApiPostUrlConstants.*;
import static fun.club.common.util.SuccessMessages.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final LoginService loginService;


    // 회원가입
    @PostMapping(USERS_SIGN_UP)
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto dto) throws Exception {
        Long user = userService.signUp(dto);
        SuccessResponse response = new SuccessResponse<>(true,SIGNUP_SUCCESS,user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    // 로그인
    @PostMapping(USERS_LOGIN)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        AuthResponse authResponse = loginService.login(loginDto);
        log.info("엑세스 토큰 : " + authResponse.getAccessToken());
        SuccessResponse<AuthResponse> response = new SuccessResponse<>(true, LOGIN_SUCCESS, authResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 회원 정보 수정
    @PatchMapping(USERS_UPDATE_PROFILE)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> uploadProfile(@ModelAttribute @Valid UserUpdateDto updateDto,
                                                @PathVariable Long userId,
                                                @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        Long user =  userService.updateProfile(updateDto,userId, profileImage);
        SuccessResponse response = new SuccessResponse<>(true,PROFILE_UPDATE_SUCCESS,user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 본인 프로필 조회
    @GetMapping(USERS_FIND_BY_ID)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserId(@PathVariable Long userId){
        UserInfoResponse id = userService.findById(userId);
        SuccessResponse response = new SuccessResponse<>(true,USER_RETRIEVE_SUCCESS,id);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    // 다른 사용자가 프로필 조회 - 이름
    @GetMapping(USERS_FIND_BY_USERNAME)
    public ResponseEntity<?> getUserNames(@RequestParam String username) {
        UserInfoResponse user = userService.findByUsername(username);
        SuccessResponse response = new SuccessResponse<>(true,USERNAME_RETRIEVE_SUCCESS,user);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
     /** 인증된(로그인중인) 사용자 조회
      *  현재 로그인한 사용자의 정보를 가져오는 동작을 해야 회원 본인 프로필을 조회가 가능하다.
      */
    @GetMapping(USERS_FIND_LOGIN_USER)
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserInfoResponse> findLoginUser() {
        // 현재 인증된 사용자 정보 가져오기
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserInfoResponse userInfo = userService.findByEmail(email);
        SuccessResponse response = new SuccessResponse(true,AUTH_USER_INFO_SUCCESS,userInfo);

        return ResponseEntity.ok(userInfo);

    }
    @DeleteMapping(USERS_DELETE_USER)
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        userService.delete(userId);
        SuccessResponse response = new SuccessResponse<>(true,USER_DELETE_SUCCESS,userId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
}


