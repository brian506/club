package fun.club.common.util;

import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

/**
 * 로그인된 사용자의 username 을 가져오기 위한 클래스
 */

public class SecurityUtil {

    public static String getLoginUsername() {
        // SecurityContextHolder에서 Authentication 객체를 가져옴
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 인증 정보가 없으면 예외 처리
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("사용자가 로그인하지 않았습니다.");
        }

        // UserDetails 객체에서 사용자 이름을 가져옴
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            // principal이 String인 경우, 사용자 이름을 직접 반환
            return (String) principal;
        }

        // 예상치 못한 principal 타입일 경우 예외 처리
        throw new IllegalStateException("사용자 정보 형식이 올바르지 않습니다.");
    }
}
