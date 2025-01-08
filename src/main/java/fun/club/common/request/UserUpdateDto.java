package fun.club.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class UserUpdateDto {

    @NotBlank(message = "필수 항목입니다")
    private String username;

    @Email
    @NotBlank(message = "옳바르지 않은 이메일 형식입니다")
    private String email;

    @NotBlank(message = "'-' 붙이지 않고 8자리로 입력하세요")
    @Length(min = 11,max = 11)
    private String phoneNumber;

    @NotBlank
    @Length(min = 8,max = 8,message = "생년월일은 8자리 숫자로 입력하세요")
    private String birth;

    @NotNull(message = "본인의 MBTI를 작성하세요")
    private String personality;

}
