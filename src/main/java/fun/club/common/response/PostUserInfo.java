package fun.club.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@AllArgsConstructor
public class PostUserInfo {

    private Long userId;

    private String title;

    private String content;

    private MultipartFile image;
}
