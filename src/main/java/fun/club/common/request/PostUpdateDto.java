package fun.club.common.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class PostUpdateDto {

    @Size(min = 1, max = 30)
    private String title;

    @Size(min = 1, max = 500)
    private String content;

    private MultipartFile file  ;

}
