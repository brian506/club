package fun.club.secure.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.club.secure.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomLoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    /**
     * 로그인 로직은 따로 Controller 에서 하는 것이 아니라 시큐리티에서 로그인과 관련된 URL을 지정해서 실행할 수 있는 필터가 있는데, 로그인과 관련된 실행은 필터에서 한다.
     */

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login";
    private static final String HTTP_METHOD = "POST";
    private static final String CONTENT_TYPE = "application/json";
    private static final String USERNAME_KEY = "email";
    private static final String PASSWORD_KEY = "password";
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);

    private final ObjectMapper objectMapper;
    private final JwtService jwtService;


    /**
     * 부모 클래스인  AbstractAuthenticationProcessingFilter 의 생성자 파라미터로 위에서 선언한 /login URL 을 설정해
     * /login 로 요청이 들어왔을때  해당 필터가 동작함,쉽게 말해 Http 요청과 응답을 가로챈다
     */
    public CustomLoginAuthenticationFilter(ObjectMapper objectMapper,JwtService jwtService) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
    }



    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {

        // application/json 형식이 아니면 예외 발생
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        //요청받은 messageBody 는 json 형식이므로 ObjectMapper 로 email 과 password 를 추출한다.
        String email = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        //인증 처리 대상이 될 UsernamePasswordAuthenticationToken 객체를 email 과 password 로 만든다.
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        // authenticationManager 는 authenticationProvider 에게 인증을 위임하고, 이는 loadUserByUserName 메서드를 호출해 사용자 정보를 로드한다.
        // DB 에서 사용자 정보를 가져오고 입력된 비밀번호와 저장된 비밀번호를 비교한다
        // 인증이 성공하면 Authentication 객체가 반환된다.
        Authentication authentication = this.getAuthenticationManager().authenticate(authRequest);

//        // 반환된 사용자 정보의 JWT 생성
//        String accessToken = jwtService.createAccessToken(email, authentication.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.joining(",")));
//        String refreshToken = jwtService.createRefreshToken();
//
//        // Refresh Token 저장 (DB 등)
//        jwtService.updateRefreshToken(email, refreshToken);
//
//        // JWT를 HTTP 응답에 추가
//        response.addHeader("Authorization", "Bearer " + accessToken);
//        response.addHeader("Refresh-Token", refreshToken);

        return authentication; // 인증 성공 시 Authentication 객체 반환
    }

}
