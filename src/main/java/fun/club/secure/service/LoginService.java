package fun.club.secure.service;


import fun.club.core.admin.domain.AdminUser;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // AdminUser 테이블에서 먼저 찾기
        Optional<AdminUser> adminUserOptional = adminUserRepository.findByEmail(email);
        if (adminUserOptional.isPresent()) {
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
