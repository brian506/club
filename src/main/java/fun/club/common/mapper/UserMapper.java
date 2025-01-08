package fun.club.common.mapper;


import fun.club.common.request.SignupRequestDto;
import fun.club.common.request.UserUpdateDto;
import fun.club.common.response.UserInfoByAdminResponse;
import fun.club.common.response.UserInfoResponse;
import fun.club.core.user.domain.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    /**
     * 1.requestDto의 password1 필드값을 user 엔티티의 password 필드에 매핑한다.
     *
     * 2.엔티티의 ID는 자동 생성되므로 무시
     *
     * 3.profileImageUrl은 서비스 계층에서 파일 업로드 처리 후 URL 을 생성해야 하므로 매핑하지 않는다.
     *   따로 서비스계층에서 저장하는 로직을 만든다.
     *
     * 4. accessCode는 회원가입 시에만 필요한 필드값이기 때문에 user 엔티티에는 들어가지 않아도 되므르 ignore
     *
     * !! User 엔티티에는 있고 DTO 에는 없는 것들은 매핑을 ignore = true 로 해줘야 한다 !!
     * //@Mapping(target = "accessCode",ignore = true) // 따로 안해도 됨?,dto에는 있고 엔티티에는 없는거는 그냥 알아서 매핑해줌
     * 받은 요청을 user의 db에 저장,보낼 응답을 user의 db에서 꺼내와서 전달
     */
    @Mapping(target = "password", source = "password1")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "absencePoint", ignore = true)
    @Mapping(target = "boards", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    @Mapping(target = "profileImageUrl", ignore = true)
    User toEntity(SignupRequestDto signupRequestDto);
    // 요청에서 보내는 건 엔티티로
    // 응답에서 보내는 건 dto로

    /**
     * 수정 관련된 Mapping 은 void 반환이 적합한 이유?
     * @MappingTarget 은 기존 객체를 직접 수정하므로 새로운 객체를 생성하지 않아도 돼서 반환값이 필요없다
     */
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "absencePoint", ignore = true)
    @Mapping(target = "boards", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "refreshToken", ignore = true)
    void fixEntity(UserUpdateDto userUpdateDto, @MappingTarget User user);

    UserInfoResponse toDto(User user);

    UserInfoByAdminResponse toAdmin(User user);
}
