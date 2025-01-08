package fun.club.common.mapper;

import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.response.BoardResponse;
import fun.club.common.response.CommentResponse;
import fun.club.core.comment.domain.Comment;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.user.domain.User;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    BoardMapper INSTANCE = Mappers.getMapper(BoardMapper.class);

    @Mapping(target = "writer",source = "writer")
    @Mapping(target = "postDetails.title", source = "postCreateDto.title")
    @Mapping(target = "postDetails.content", source = "postCreateDto.content")
    FreeBoard freeBoardFromDto(PostCreateDto postCreateDto,User writer);
    // dto 에서의 값을 postDetails 의 값에 매핑 시키는 것(값을 옮기는 느낌)

    @Mapping(target = "writer",source = "writer")
    @Mapping(target = "postDetails.title", source = "postCreateDto.title")
    @Mapping(target = "postDetails.content", source = "postCreateDto.content")
    NoticeBoard noticeBoardFromDto(PostCreateDto postCreateDto,User writer);


    void updateBoardFromDto(PostUpdateDto postUpdateDto, @MappingTarget NoticeBoard noticeBoard);
    void updateBoardFromDto(PostUpdateDto postUpdateDto, @MappingTarget FreeBoard freeBoard);

    @Mapping(target = "title",source = "board.postDetails.title")
    @Mapping(target = "content",source = "board.postDetails.content")
    @Mapping(target = "id",source = "board.id")
    @Mapping(target = "writer",source = "board.writer.username")
    @Mapping(target = "commentResponses",source = "board.comments")
    BoardResponse responseToDto(Board board);


    // Comment 객체를 CommentResponse로 변환하는 메서드
    CommentResponse commentToResponse(Comment comment);
    /**
     * 특정 게시글에 있는 댓글들을 조회하기 위해서는 commentResponse 를 따로 만들어서 원하는 정보만 클라이언트가 받을 수 있게 한다.
     * BoardResponse 안에 있는 List<CommentResponse> 를 BoardResponse 와 매핑하려면 먼저 Comment 객체를 받아오고
     * 그다음에 commentResponses 객체를 타게팅해서 매핑해준다.
     */
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
 */

/**
 * source : 매핑이 될 객체. getter 필요
 * target : 매핑할 객체, 생성자와 setter 필요
 */