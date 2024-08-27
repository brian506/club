package fun.club.api.user;

import fun.club.core.common.request.LoginDto;
import fun.club.core.common.request.RefreshTokenRequest;
import fun.club.core.common.response.AuthResponse;
import fun.club.secure.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        // 인증 성공시
        String accessToken = jwtService.createAccessToken(loginDto.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        return ResponseEntity.ok(new AuthResponse(accessToken, refreshToken));
    }
}

//    @PostMapping("/refresh")
//    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest request) {
//
//        String email = jwtService.getAccessHeader();
//        String newAccessToken = jwtService.createRefreshToken();
//
//        return ResponseEntity.ok(new AuthResponse(newAccessToken, request.getRefreshToken()));
//    }
//}
