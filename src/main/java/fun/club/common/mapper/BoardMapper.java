package fun.club.common.mapper;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.user.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "writer", expression = "java(writer)")
    @Mapping(target = "postDetails.title", source = "postCreateDto.title")
    @Mapping(target = "postDetails.content", source = "postCreateDto.content")
    FreeBoard freeBoardFromDto(PostCreateDto postCreateDto, User writer);
    // dto 에서의 값을 postDetails 의 값에 매핑 시키는 것(값을 옮기는 느낌)

    @Mapping(target = "image",  ignore = true)
    @Mapping(target = "writer", expression = "java(writer)")
    @Mapping(target = "postDetails.title", source = "postCreateDto.title")
    @Mapping(target = "postDetails.content", source = "postCreateDto.content")
    NoticeBoard noticeBoardFromDto(PostCreateDto postCreateDto, User writer);

    @Mapping(target = "postDetails.title", source = "postUpdateDto.title")
    @Mapping(target = "postDetails.content", source = "postUpdateDto.content")
    void updateBoardFromDto(PostUpdateDto postUpdateDto, @MappingTarget NoticeBoard noticeBoard);

    @Mapping(target = "postDetails.title", source = "postUpdateDto.title")
    @Mapping(target = "postDetails.content", source = "postUpdateDto.content")
    void updateBoardFromDto(PostUpdateDto postUpdateDto, @MappingTarget FreeBoard freeBoard);

}

/**
 * postDetails 엔티티 필드값에 dto 필드값이 없으면 자동으로 매피해주지 않는다.(impl 에서 설정해야됨)
 * mapper 를 쓸 떄 해당 클래스에 @Builder 입력해야함
 * @Builder 를 사용하면 기본적으로 인자없는 생성자 필요(AllArgsConstructor)
 *
 * 임베디드 클래스 postDetails 의 필드값을 타겟으로 하고 엔티티로 변환
 */

/**
  @MappingTarget 은 이미 존재하는 객체에 매핑할 때 사용
 기본적으로 mapstruct 는 새로운 객체를 생성하지만, 위 어노테이션은 기존 객체의 필드값만 변경 가능
 *
 */
