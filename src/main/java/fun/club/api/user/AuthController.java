package fun.club.api.user;

import fun.club.core.common.request.LoginDto;
import fun.club.core.common.request.RefreshTokenRequest;
import fun.club.core.common.response.AuthResponse;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

//@RestController
//@RequestMapping("/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final JwtService jwtService;
//    private final UserRepository userRepository;
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
//
//        // 인증 성공시
//        String accessToken = jwtService.createAccessToken(loginDto.getEmail());
//        String refreshToken = jwtService.createRefreshToken();
//
//        // db 에 저장
//        jwtService.updateRefreshToken(loginDto.getEmail(), refreshToken);
//
//        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
//    }
//    }


//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
//        // 요청에서 리프레시 토큰 가져오기
//        String refreshToken = request.getRefreshToken();
//
//       jwtService.updateRefreshToken();
//
//        // 새로운 액세스 토큰 발급
//        String newAccessToken = jwtService.createAccessToken(email);
//
//        // 응답 반환
//        return ResponseEntity.ok(new AuthResponse(newAccessToken, refreshToken));
//    }
//}