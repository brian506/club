package fun.club.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    // 사용자가 다른 사용자 보고 싶을 때 보내는 응답
    private Long id;

    private String username;

    private String birth;

    private String personality;

    private String profileImageUrl;
}
