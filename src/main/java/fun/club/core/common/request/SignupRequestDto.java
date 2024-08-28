package fun.club.core.common.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
public class SignupRequestDto {

    @NotBlank(message = "필수 항목입니다")
    private String username;

    @Email
    @NotBlank(message = "옳바르지 않은 이메일 형식입니다")
    private String email;

    @NotBlank
    @Size(min = 8,message = "비밀번호는 8자리 이상으로 입력하세요")
    private String password1;

    private String password2;

    @NotBlank(message = "'-' 붙이지 않고 8자리로 입력하세요")
    @Length(min = 8,max = 8)
    private String phoneNumber;

    @NotBlank
    @Length(min = 8,max = 8,message = "생년월일은 8자리 숫자로 입력하세요")
    private String birth;

    @NotNull(message = "본인의 MBTI를 작성하세요")
    private String personality;

    @NotBlank(message = "필수 항목입니다")
    private String joinCode;
    // user 엔티티에는 필요없음(DB에 저장X)

    private MultipartFile profileUrl;
}
// dto는 데이터 전송만을 하기 때문에 비밀번호 더블체크 메서드는 service 계층에서 만들자