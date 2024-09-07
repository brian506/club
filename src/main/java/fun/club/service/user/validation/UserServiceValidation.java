package fun.club.service.user.validation;

import fun.club.common.request.SignupRequestDto;

public interface UserServiceValidation {
    void validateSignUp(String email) throws Exception;

    void validatePassword(String password1,String password2) throws Exception;

    void validateCode(String code) throws Exception;

    void validateAll(SignupRequestDto dto) throws Exception;
    /**
     * 3가지의 유효성 검사를 validateAll 으로 한번에 묶어냈다.
     */
}
