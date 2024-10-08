package fun.club.secure.service;


import fun.club.common.request.LoginDto;
import fun.club.common.response.AuthResponse;
import fun.club.core.admin.domain.AdminUser;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {
    // filter 에서는 로그인한 사용자 정보를 추출하고 loginService 에서는 DB 에 저장된 사용자 정보를 추출해서 서로 비교한다

    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;
    private final JwtService jwtService;

    public AuthResponse login(LoginDto loginDto){
        UserDetails userDetails = loadUserByUsername(loginDto.getEmail());

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElseThrow(() -> new IllegalArgumentException("사용자 역할 정보가 없습니다."));

        String accessToken = jwtService.createAccessToken(userDetails.getUsername(),role);
        String refreshToken = jwtService.createRefreshToken();

        jwtService.updateRefreshToken(loginDto.getEmail(), refreshToken);
        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", email);
        // AdminUser 테이블에서 먼저 찾기
        Optional<AdminUser> adminUserOptional = adminUserRepository.findByEmail(email);

        if (adminUserOptional.isPresent()) {
            log.info("AdminUser found: {}", email);
            AdminUser adminUser = adminUserOptional.get();
            return org.springframework.security.core.userdetails.User.builder()
                    .username(adminUser.getEmail())
                    .password(adminUser.getPassword())
                    .roles("ADMIN") // Admin role 설정
                    .build();
        }

        // 일반 User 테이블에서 찾기
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("해당 이메일로 가입된 사용자가 없습니다. 이메일 : {}",email);
                    return new UsernameNotFoundException("해당 이메일이 존재하지 않습니다.");

                });
        log.info("User found: {}", email);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // User role 설정
                .build();
    }
}
/**
 * AdminUser 가 존재하면 User 를 조회하지 않고, AdminUser 가 존재하지 않으면 User 를 조회한다.
 * 이는 하나의 이메일이 AdminUser와 User 둘 다에 존재하지 않는 가정하에 동작한다.
 */
