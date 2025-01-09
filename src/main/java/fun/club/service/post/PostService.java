package fun.club.service.post;


import fun.club.common.request.PostCreateDto;
import fun.club.common.request.PostUpdateDto;
import fun.club.common.response.BoardResponse;
import fun.club.core.post.domain.Board;
import fun.club.core.post.domain.FreeBoard;
import fun.club.core.post.domain.NoticeBoard;
import fun.club.core.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


public interface PostService  {

    Long create(PostCreateDto postCreateDto) throws IOException;

    Long update(PostUpdateDto postUpdateDto,Long postId) throws IOException;

    void delete(Long postId);

    Page<BoardResponse> findAllByWriter(Long userId, Pageable pageable) throws IOException;

    List<BoardResponse> findByTitle(String title);

    Page<BoardResponse> findAllFromBoard(int pageNo, int pageSize, String criteria);



}
// 공통된 메서드를 처리할 때 이렇게 인터페이스와 구현체를 구분해서 하면 유지보수에 좋다
// 컨트롤러에서 쓰임

/**
 * postDetails (게시물 작성) 과 자유게시판,공지게시판을 서로 어떻게 처리할까에 대한 고민을 많이 했다.
 *
 * 처음으로 접근한 것은 postDetails 와 게시판들을 다대일로 연관관계를 설정해주고 매핑해주었다.
 * 이렇게 되면 pk 는 postDetails 가 갖고 id 값들을 통해 게시판을 조회하는 방식이었다.
 * 이렇게 하면 게시판에 게시물 생성에 필요한 필드값들은 나타나지 않고, 게시판의 엔티티를 설정해주고 테이블을 만들어주는 이유가 없어진다.
 * 처음에는 postDetails 를 추상클래스로 하던지. embeddable 로 하던지 고민을 해봤지만, 중복되는 코드가 많아지고 유지보수가 어려울 것 같아서 그렇게 하지 않았다.
 * 하지만 이번 프로젝트가 큰 서비스를 만드는 것도 아니고, 중복되는 코드도 impl 같이 인터페이스로 관리해서 서비스 계층을 구현해보는 것도 좋은 경험이 될 것 같아서
 * postDetails 를 추상클래스로 하고 두 개의 게시판이 상속받도록 구현하려고 했지만, 게시판에 따른 기능이 크게 다르지 않기 때문에 복잡하게 가지 않는 것이 좋다고 생각했다.
 * 그리고 mapper 를 사용할 때 추상클래스인 postDetails 를 mapping 할 수 없어서 어차피 또 중복된 코드가 생기기 마련이었다.
 * 그래서 postDetails 를 임베디드 클래스로 해서 공통된 필드값을 넣을 것이다.
 */