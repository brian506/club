package fun.club.service.user.validation;

import fun.club.common.request.SignupRequestDto;
import fun.club.common.response.UserInfoResponse;
import fun.club.core.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceValidationImpl implements UserServiceValidation {

    private final UserRepository userRepository;

    private static final String ALREADY_EXIST_EMAIL_ERROR = "이미 존재하는 이메일입니다.";
    private static final String NOT_THE_SAME_PASSWORD = "비밀번호가 일치하지 않습니다.";
    private static final String NOT_THE_SAME_CODE = "가입코드가 일치하지 않습니다.";

    @Value("${club.join-code}")
    private String clubJoinCode;

    @Override
    public void validateSignUp(String email) throws Exception {
        if (userRepository.findByEmail(email).isPresent()){
            throw new Exception(ALREADY_EXIST_EMAIL_ERROR);
        }
    }

    @Override
    public void validatePassword(String password1,String password2) throws Exception {
        if (!password1.equals(password2)){
            throw new Exception(NOT_THE_SAME_PASSWORD);
        }
    }

    @Override
    public void validateCode(String code) throws Exception {
        if (!code.equals(clubJoinCode)){
            throw new Exception(NOT_THE_SAME_CODE);
        }
    }
    public void validateAll(SignupRequestDto dto) throws Exception {
        validateSignUp(dto.getEmail());
        validatePassword(dto.getPassword1(),dto.getPassword2());
        validateCode(dto.getJoinCode());
    }
}
/**
 * 인터페이스와 구현체로 나눈 이유
 * 인터페이스를 사용하면 여러 구현체를 가질 수 있으므로 기존 코드를 수정하지 않고 다른 새로운 구현 클래스를 만들거나 교체하면 되므로 유연해진다.
 */

/**
 * 추상클래스와 인터페이스의 차이?
 * 추상클래스
 * 부모 클래스를 상속받는 자식 클래스 간에 공통 기능을 구현하고, 확장하며 상속과 관련
 *
 * 인터페이스
 * 상속관계에 얽매이지 않고, 공통 기능이 필요할 때 추상메서드로 여러 가지 형태로 구현할 수 있는 다형성과 관련
 */