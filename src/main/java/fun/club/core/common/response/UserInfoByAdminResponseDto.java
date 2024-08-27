package fun.club.core.common.response;


import fun.club.core.user.domain.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoByAdminResponseDto {
    // 임원이 사용자 정보 볼때 필요한 응답

    private Long id; // 식별자 값으로 조회하기 위함

    private String username;

    private String email;

    private String birth;

    private String personality;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;




}
