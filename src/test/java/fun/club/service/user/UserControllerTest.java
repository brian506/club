package fun.club.service.user;

import fun.club.api.user.UserController;
import fun.club.core.user.domain.Role;
import fun.club.core.user.domain.User;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.service.JwtService;
import fun.club.service.file.FileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private FileService fileService;

    @Autowired
    private JwtService jwtService;

    /**
     * 프로필 수정 기능은 인증/권한 처리가 중요한 부분이기 때문에 단순한 서비스단에 단위 테스트보다는
     * 통합테스트를 통해 컨트롤러부터 서비스까지의 전체 흐름을 테스트 하는 게 적합
     *
     * 인증/권한 -> API 흐름 -> 실제 데이터베이스에서의 확안
     *
     */
    @Test
    void 프로필수정_통합테스트() throws Exception {
        //given
        // DB에 새로운 테스트 user 생성
        User user = userRepository.save(User.builder()
                    .username("최영민")
                .email("br@naver.com")
                .role(Role.USER)
                .phoneNumber("01020504471")
                .birth("20000506")
                .personality("CUTE")
                .build());

//        // 테스트 환경에서 파일 업로드를 모방하기 위한 file 객체
//        MockMultipartFile mockFile = new MockMultipartFile(
//                "profileImage", // 요청에서 받을 필드 이름
//                "newImage.jpg", // 파일 이름
//                "image/jpeg", // 파일 타입
//                new byte[]{1,2,3,4} // 파일 내용
//        );
//
//        when(fileService.saveFile(any(MultipartFile.class))).thenReturn("newImage.jpg");
//        doNothing().when(fileService).deleteFile(eq("oldImage.jpg"),eq(true));

        String token = "Bearer " + jwtService.createAccessToken(user.getEmail(), String.valueOf(user.getRole()));

        //when
         mockMvc.perform(multipart("/v1/api/users/profile") // API 경fh
                        .param("username", "최영민")
                        .param("email", "br@naver.com")
                        .param("phoneNumber", "01098765432")
                        .param("birth", "19900506")
                        .param("personality", "HAPPY")
                        .header("Authorization", token) // 인증 헤더 추가
                        .with(request -> {
                            request.setMethod("PATCH");
                            return request; // 기본값이 POST 이므로 강제로 HTTP 메서드를 바꿔줘야함
                        })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk()) // 응답 코드 확인
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원 정보 수정 성공"))
                .andExpect(jsonPath("$.data.email").value("br@naver.com"));

        //then : DB 상태 확인
        User updatedUser = userRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        assertEquals("br@naver.com", updatedUser.getEmail());
        assertEquals("01098765432", updatedUser.getPhoneNumber());
        assertEquals("19900506", updatedUser.getBirth());
    }
}
/**
 * 통합테스트에서는 보통 스프링 컨텍스트를 통해 userService,userRepository,fileService 등이 자동으로 생성
 * -> injectMock 을 사용안해도 됨
 *
 */