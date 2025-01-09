package fun.club.service.user;

import fun.club.common.mapper.UserMapper;
import fun.club.common.response.UserInfoResponse;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserServiceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    @Test
    void 회원조회() throws Exception {
        //given
        User savedUser = userRepository.save(User.builder()
                        .id(1L)
                .username("최영민")
                .birth("20000506")
                .personality("ISFH")
                .profileImageUrl("profile.jpg")
                .build());
        //when
        UserInfoResponse response = userService.findById(savedUser.getId());

        //then
        assertNotNull(response); // 응답이 null이 아닌지 확인
        assertEquals(savedUser.getId(), response.getId()); // ID 검증
        assertEquals("최영민", response.getUsername()); // Username 검증
        assertEquals("20000506", response.getBirth()); // Birth 검증
        assertEquals("ISFH", response.getPersonality()); // Personality 검증
        assertEquals("profile.jpg", response.getProfileImageUrl()); // Profile Image 검증
    }
}
