package fun.club.service.user;

import fun.club.common.mapper.UserMapper;
import fun.club.common.request.SignupRequestDto;
import fun.club.common.response.UserInfoResponse;
import fun.club.common.util.OptionalUtil;
import fun.club.core.admin.domain.AdminUser;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.domain.Role;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.service.file.FileService;
import fun.club.service.user.validation.UserServiceValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceValidation validation;
    private final FileService fileService;
    private final AdminUserRepository adminUserRepository;


    public Long signUp(SignupRequestDto dto) throws Exception {

        validation.validateAll(dto);

        User user = userMapper.toEntity(dto);

        encodeAndSetPassword(user, dto.getPassword1());

        if (userRepository.count() == 0){
            user.setRole(Role.ADMIN);
        }else user.setRole(Role.USER);
        // 처음 회원가입 한 회원은 ADMIN, 나머진 다 USER

        return userRepository.save(user).getId();
    }

    // ADMIN 권한 부여
    public void assignAdminRole(Long userId){
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId),"등록되지 않은 회원 ID 입니다");

//        user.setRole(Role.ADMIN);
        userRepository.delete(user);

        AdminUser admin = AdminUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        admin.setAssignedAt(LocalDateTime.now());

        adminUserRepository.save(admin);
        // user 인 회원이 admin 으로 권한 변경되면 adminUser 엔티티에 저장
    }

    /**
     * 비밀번호 인코딩(암호화)
     */
    private void encodeAndSetPassword(User user, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        user.passwordEncode(encodedPassword);
    }
    /**
     * 프로필 사진 업로드
     */
    public void updateProfile(Long userId, MultipartFile profileImage) throws IOException {

        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId),"존재하지 않는 회원 ID 입니다");

        // 기존 파일 삭제 (있는 경우)
        if (user.getProfileImageUrl() != null) {
            fileService.deleteFile(user.getProfileImageUrl(),true);
        }

        // 새 파일 저장
        String fileName = fileService.saveFile(profileImage);
        user.setProfileImageUrl(fileName);

        userRepository.save(user);
    }
    public String getProfileImageUrl(Long userId) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId), "존재하지 않는 회원 ID 입니다");
        return user.getProfileImageUrl();
    }


    /**
     *   본인,남이 다른 사람 프로필 조회
     */

    // id 로 회원 조회
    public UserInfoResponse findById(Long id) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id),"존재하지 않는 회원 ID 입니다.");
        return userMapper.toDto(user);
    }

    // 이름으로 회원 조회
    public UserInfoResponse findByUsername(String username) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findByUsername(username),"존재하지 않는 회원 이름입니다.");
        return userMapper.toDto(user);
    }

    public UserInfoResponse findByEmail(String email) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(email),"존재하지 않는 이메일입니다.");
        return userMapper.toDto(user);
    }

    // 회원 탈퇴
    public void delete(Long id) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id), "존재하지 않는 회원입니다.");
        userRepository.delete(user);
    }

}

