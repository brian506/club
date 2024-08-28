package fun.club.api.user;

import fun.club.core.common.request.LoginDto;
import fun.club.core.common.request.SignupRequestDto;
import fun.club.core.common.response.AuthResponse;
import fun.club.core.common.util.SuccessResponse;
import fun.club.secure.service.JwtService;
import fun.club.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(produces  = "application/json; charset=UTF-8")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;


    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequestDto dto) throws Exception {
        Long user = userService.signUp(dto);
        SuccessResponse response = new SuccessResponse<>(true,"회원 등록 성공",user);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        // 인증 성공시
        String accessToken = jwtService.createAccessToken(loginDto.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        // db 에 저장
        jwtService.updateRefreshToken(loginDto.getEmail(), refreshToken);

        System.out.println(new AuthResponse(accessToken, refreshToken));

        SuccessResponse response = new SuccessResponse<>(true,"로그인 성공",refreshToken);

        return new ResponseEntity<>(response,HttpStatus.OK);

    }
}


