package fun.club.core.common.mapper;


import fun.club.core.common.request.SignupRequestDto;
import fun.club.core.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;



@Mapper(componentModel = "spring")
public interface UserMapper {
    // 요청에서 보내는 건 엔티티로
    // 응답에서 보내는 건 dto로
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", source = "password1")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "profileImageUrl",ignore = true)
//    @Mapping(target = "accessCode",ignore = true) // 따로 안해도 됨?
    User toEntity(SignupRequestDto signupRequestDto);

    /**
     * 1.requestDto의 password1 필드값을 user 엔티티의 password 필드에 매핑한다.
     * 2.엔티티의 ID는 자동 생성되므로 무시
     * 3.profileImageUrl은 따로 service 계층에서 할 것이기 떄문에 매핑하지 않느다.
     * 4. accessCode는 회원가입 시에만 필요한 필드값이기 때문에 user 엔티티에는 들어가지 않아도 되므르 ignore
     */
}
