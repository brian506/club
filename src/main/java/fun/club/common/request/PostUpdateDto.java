package fun.club.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostUpdateDto {

    private Long postId;

    @NotBlank
    @Size(min = 1, max = 30)
    private String title;

    @NotBlank
    @Size(min = 1, max = 500)
    private String content;

    private MultipartFile image;

}
