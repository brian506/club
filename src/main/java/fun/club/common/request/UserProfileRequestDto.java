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
/**
 * 사용자가 조회할 때는 첨부파일을 String 타입으로 요청하고
 * 사용자가 생성하거나 수정할 때는 첨부파일을 MultipartFile 로 불러온다.
 */