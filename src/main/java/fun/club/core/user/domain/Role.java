package fun.club.core.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String value;
}
/**
 * 회원가입 후에 ADMIN 으로 지정하거나 USER 로 나중에 변경할 수 있게
 */