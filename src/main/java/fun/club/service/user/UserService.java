package fun.club.service.user;

import fun.club.core.common.mapper.UserMapper;
import fun.club.core.common.request.SignupRequestDto;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.service.JwtService;
import fun.club.service.user.validation.UserServiceValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceValidation validation;
    private final JwtService jwtService;

    public Long signUp(SignupRequestDto dto) throws Exception {
        validation.validateSignUp(dto.getEmail(), dto.getUsername());

        validation.validatePassword(dto.getPassword1(), dto.getPassword2());

        validation.validateCode(dto.getJoinCode());

        User user = userMapper.toEntity(dto);

        encodeAndSetPassword(user, dto.getPassword1());

        return userRepository.save(user).getId();
    }
    /**
     * 비밀번호 인코딩(암호화)
     */
    private void encodeAndSetPassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.passwordEncode(encodedPassword);
    }
}
    //본인 프로필 조회

    // 다른 회원 조회(이름으로)

    // 본인 프로필 수정

    // 회원 탈퇴



