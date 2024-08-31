package fun.club.common.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserProfileRequestDto {
    // 회원 권한 사용자가 다른 사람 정보 보고 싶을 때 보내는 요청
    // 회원 게시판에서 사용
    private String username;

    private String birth;

    private String personality;

    private String profileImageUrl;

}
