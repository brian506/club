package fun.club.common.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardResponse {

    private Long id;
    private String title;
    private String content;
    private String writerName;

}
