package fun.club.secure.filter;

import fun.club.core.admin.domain.AdminUser;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


/**
 * 이 클래스에서는 토큰들을 검증해서 인증 처리 실패,재발급등의 역할을 수행
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;
    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            if (request.getRequestURI().equals(NO_CHECK_URL)) { // 실패,재발급
                filterChain.doFilter(request, response);
                return;
            }
            // 엑세스 토큰 추출 및 유효성 검사
            Optional<String> accessToken = jwtService.extractAccessToken(request)
                    .filter(jwtService::isTokenValid);

            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter(jwtService::isTokenValid)
                    .orElse(null);


            // 리프레시 토큰이 존재하고 엑세스 토큰이 없거나 유효하지 않은 경우에만 엑세스 토큰 재발급
            if (refreshToken != null && accessToken.isEmpty()) {
                checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
                return;
            }

            if (accessToken.isPresent()) { // 엑세스 토큰이 유효한 경우 인증 처리
                checkAccessTokenAndAuthentication(request, response, filterChain);
            }else filterChain.doFilter(request,response); // 둘다 없을 경우 필터 체인 반복
        }


    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        // AdminUser 먼저 조회
        adminUserRepository.findByRefreshToken(refreshToken).ifPresentOrElse(
                adminUser -> {
                    String reIssuedRefreshToken = reIssueRefreshTokenForAdmin(adminUser);
                    jwtService.sendAccessAndRefreshToken(
                            response,
                            jwtService.createAccessToken(adminUser.getEmail(), String.valueOf(adminUser.getRole())),
                            reIssuedRefreshToken
                    );
                },
                () -> {
                    // AdminUser가 없으면 일반 User 조회
                    userRepository.findByRefreshToken(refreshToken).ifPresent(user -> {
                        String reIssuedRefreshToken = reIssueRefreshTokenForUser(user);
                        jwtService.sendAccessAndRefreshToken(
                                response,
                                jwtService.createAccessToken(user.getEmail(), String.valueOf(user.getRole())),
                                reIssuedRefreshToken
                        );
                    });
                }
        );
    }


    private String reIssueRefreshTokenForAdmin(AdminUser adminUser) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        adminUser.updateRefreshToken(reIssuedRefreshToken);
        adminUserRepository.saveAndFlush(adminUser);
        return reIssuedRefreshToken;
    }

    private String reIssueRefreshTokenForUser(User user) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);
        return reIssuedRefreshToken;
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken)
                        .ifPresent(email -> {
                            // AdminUser 조회
                            Optional<AdminUser> adminUserOptional = adminUserRepository.findByEmail(email);
                            if (adminUserOptional.isPresent()) {
                                saveAuthenticationForAdmin(adminUserOptional.get(),accessToken);
                            }

                            // User 조회 (AdminUser가 없거나 관계없이)
                            Optional<User> userOptional = userRepository.findByEmail(email);
                            userOptional.ifPresent(user -> saveAuthenticationForUser(user,accessToken));
                        }));

        filterChain.doFilter(request, response);
    }

    public void saveAuthenticationForAdmin(AdminUser adminUser,String accessToken) {
        String password = adminUser.getPassword();
        if (password == null) {
            password = "defaultAdminPassword"; // AdminUser의 기본 비밀번호 설정 (필요에 따라 변경)
        }

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(adminUser.getRole().getValue()));

        UserDetails adminUserDetails = org.springframework.security.core.userdetails.User.builder()
                .username(adminUser.getEmail())
                .password(password)
                .authorities(authorities)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                adminUserDetails, null, authoritiesMapper.mapAuthorities(adminUserDetails.getAuthorities())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void saveAuthenticationForUser(User user,String accessToken) {
        String password = user.getPassword();
        if (password == null) {
            password = "defaultUserPassword"; // User의 기본 비밀번호 설정 (필요에 따라 변경)
        }

        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole().getValue()));

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(password)
                .authorities(authorities)
                .build();

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetailsUser, null, authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}