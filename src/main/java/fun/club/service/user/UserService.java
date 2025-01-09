package fun.club.service.user;

import fun.club.common.mapper.UserMapper;
import fun.club.common.request.SignupRequestDto;
import fun.club.common.request.UserUpdateDto;
import fun.club.common.response.UserInfoResponse;
import fun.club.common.util.OptionalUtil;
import fun.club.common.util.SecurityUtil;
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

        User savedUser = userRepository.save(user);

        if (userRepository.count() == 1){ // 첫 사용자일 때
            user.setRole(Role.ADMIN);
            AdminUser admin = AdminUser.builder()
                    .id(savedUser.getId()) // ID는 새로 생성된 ID를 사용해야 합니다
                    .username(savedUser.getUsername())
                    .password((user.getPassword()))
                    // 사용자의 비번을 인코딩한 후 user 객체에 저장하고 후에 adminUser 를 생성할 때 비밀번호를 다시 인코딩하면 user 와 adminUser의 비번이 다를 수 있으므로 이미 인코딩된 비번을 사용해야함
                    .email(savedUser.getEmail())
                    .phoneNumber(savedUser.getPhoneNumber())
                    .profileImageUrl(savedUser.getProfileImageUrl())
                    .role(Role.ADMIN)
                    .assignedAt(LocalDateTime.now())
                    .build();
            adminUserRepository.save(admin); // adminUser 저장

        }else user.setRole(Role.USER);
        // 처음 회원가입 한 회원은 ADMIN, 나머진 다 USER

        return savedUser.getId();
    }

    // ADMIN 권한 부여
    public void assignAdminRole(Long userId){
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId),"존재하지 않는 회원입니다.");

//        user.setRole(Role.ADMIN);
        userRepository.delete(user);

        AdminUser admin = AdminUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password((user.getPassword()))
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .profileImageUrl(user.getProfileImageUrl())
                .assignedAt(LocalDateTime.now())
                .role(Role.ADMIN)
                .build();

        adminUserRepository.save(admin);
        // user 인 회원이 admin 으로 권한 변경되면 adminUser 엔티티에 저장
    }
    // db 에 대한 작업많 하기 때문에 반환값이 필요없으므로 void

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
    @Transactional
    public void uploadImage(Long userId,MultipartFile profileImage) throws IOException {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId),"존재하지 않는 회원 ID 입니다");
        // 기존 파일 삭제 (있는 경우)
        if (user.getProfileImageUrl() != null) {
            fileService.deleteFile(user.getProfileImageUrl(),true);
        }
        // 새 파일 저장
        String fileName = fileService.saveFile(profileImage);
        user.setProfileImageUrl(fileName);
    }

    /**
     * 회원정보수정
     */
    @Transactional
    public void updateProfile(UserUpdateDto updateDto) throws IOException {
        String currentUserEmail = SecurityUtil.getLoginUsername();
        User user = OptionalUtil.getOrElseThrow(userRepository.findByEmail(currentUserEmail),"존재하지 않는 회원 ID 입니다");
        user.updateProfile(
                updateDto.getUsername(),
                updateDto.getEmail(),
                updateDto.getPhoneNumber(),
                updateDto.getBirth(),
                updateDto.getPersonality()
        );
        // JPA의 변경 감지로 자동 저장(더티 체킹)
    }

    public String getProfileImageUrl(Long userId) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(userId), "존재하지 않는 회원 ID 입니다");
        return user.getProfileImageUrl();
    }


    /**
     *   본인,남이 다른 사람 프로필 조회
     */

    // id 로 회원 조회
    @Transactional(readOnly = true)
    public UserInfoResponse findById(Long id) {
        User user = OptionalUtil.getOrElseThrow(userRepository.findById(id),"존재하지 않는 회원 ID 입니다.");
        return userMapper.toDto(user);
    } // UserInfoResponse 형태로 사용자에게 다시 응답하기 위해서 반환값을 UserInfoResponse 로 함

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

