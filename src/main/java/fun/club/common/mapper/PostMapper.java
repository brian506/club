package fun.club.common.mapper;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.core.post.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "image", ignore = true)
    Post toEntity(PostCreateDto postCreateDto);


    @Mapping(target = "id",ignore = true)
    Post toEntity(PostUpdateDto postUpdateDto);
}
/**
 * post 엔티티 필드값에 dto 필드값이 없으면 자동으로 매피해주지 않는다.(impl 에서 설정해야됨)
 * mapper 를 쓸 떄 해당 클래스에 @Builder 입력해야함
 * @Builder 를 사용하면 기본적으로 인자없는 생성자 필요(AllArgsConstuctor)
 */
