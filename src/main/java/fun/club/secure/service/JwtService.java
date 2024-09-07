package fun.club.secure.service;



import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fun.club.common.response.AuthResponse;
import fun.club.common.util.SuccessResponse;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class JwtService {

    /**
     * 앞에 club. 접두사를 붙여야 함
     */
    @Value("${club.jwt.secretKey}")
    private String secretKey;

    @Value("${club.jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${club.jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${club.jwt.access.header}")
    private String accessHeader;

    @Value("${club.jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer";

    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;

    // AccessToken 생성 메서드
    public String createAccessToken(String email) {
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    // RefreshToken 생성 메서드
    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
                .sign(Algorithm.HMAC512(secretKey));
    }

    // AccessToken 헤더에 실어서 보내기
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader(accessHeader, accessToken.trim());
        log.info("재발급된 Access Token : {}", accessToken);
    }

    // 로그인 시 AccessToken과 RefreshToken을 헤더에 실어서 내보냄
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    // RefreshToken 추출
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, "").trim());
    }

    // AccessToken 추출
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, "").trim());
    }

    // AccessToken에서 이메일 추출
    public Optional<String> extractEmail(String accessToken) {
        log.info("엑세스 토큰에서 리프레시 토큰이 정상적으로 추출되었습니다");
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken.trim())
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

    // AccessToken 헤더 설정
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, "Bearer " + accessToken.trim());
    }

    // RefreshToken 헤더 설정
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, "Bearer " + refreshToken.trim());
    }

    // refreshToken을 DB에 업데이트하는 메서드
    public void updateRefreshToken(String email, String refreshToken) {
        // AdminUser에서 이메일로 조회
        adminUserRepository.findByEmail(email).ifPresentOrElse(
                adminUser -> {
                    adminUser.updateRefreshToken(refreshToken);
                    adminUserRepository.saveAndFlush(adminUser);
                },
                () -> {
                    // AdminUser가 없으면 User에서 이메일로 조회
                    userRepository.findByEmail(email).ifPresentOrElse(
                            user -> {
                                user.updateRefreshToken(refreshToken);
                                userRepository.saveAndFlush(user);
                            },
                            () -> {
                                throw new NoSuchElementException("일치하는 회원이 없습니다.");
                            }
                    );
                }
        );
    }

    // 토큰 유효성 검사 메서드
    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token.trim());
            return true;
        } catch (JWTVerificationException e) {
            log.error("유효하지 않은 토큰입니다. {}", e.getMessage());
            return false;
        }
    }
    /**
     * 토큰 앞에 공백을 없애야 한다.
     */
}