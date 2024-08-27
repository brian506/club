package fun.club.service.user.validation;

public interface UserServiceValidation {
    void validateSignUp(String email,String username) throws Exception;

    void validatePassword(String password1,String password2) throws Exception;

    void validateCode(String code) throws Exception;
}
