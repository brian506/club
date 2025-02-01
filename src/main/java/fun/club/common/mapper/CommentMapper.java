package fun.club.common.mapper;

import fun.club.common.request.CommentRequest;
import fun.club.common.response.CommentResponse;
import fun.club.common.util.OptionalUtil;
import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Board;
import fun.club.core.post.repository.BoardRepository;
import fun.club.core.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "id",ignore = true)
    @Mapping(target = "writer",source = "writer")
    @Mapping(target = "board",source = "board")
    Comment toEntity(CommentRequest commentRequest,User writer,Board board);

    @Mapping(target = "createTime",ignore = true)
    @Mapping(target = "updateTime",ignore = true)
    @Mapping(target = "username",ignore = true)
    CommentResponse commentToDto(Comment comment);


}

/**
 * comment 클래스의 board 속성을 request 에서 만들어준 새로운 board 에 매핑해준다.
 */
