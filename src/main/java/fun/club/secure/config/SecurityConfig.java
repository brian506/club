package fun.club.secure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import fun.club.core.admin.repository.AdminUserRepository;
import fun.club.core.user.domain.Role;
import fun.club.core.user.repository.UserRepository;
import fun.club.secure.filter.CustomLoginAuthenticationFilter;
import fun.club.secure.filter.JwtAuthenticationFilter;
import fun.club.secure.handler.LoginFailureHandler;
import fun.club.secure.handler.LoginSuccessHandler;
import fun.club.secure.service.JwtService;
import fun.club.secure.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // @PreAuthorize 등 권한에 대한 작업 처리
public class SecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AdminUserRepository adminUserRepository; // 추가된 AdminUserRepository
    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .csrf(csrfConfig -> csrfConfig.disable())
                .headers(headerConfig -> headerConfig.frameOptions(frameOptionsConfig -> frameOptionsConfig.disable()))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/users").permitAll()
                       .requestMatchers("/admins/**").hasRole("ADMIN")
                        .anyRequest().permitAll()
                );

       http.addFilterAfter(customJsonUsernamePasswordAuthenticationFilter(), LogoutFilter.class);
       http.addFilterBefore(jwtAuthenticationProcessingFilter(), CustomLoginAuthenticationFilter.class);

        return http.build();
    }
    // CustomLogin (1)
    @Bean
    public CustomLoginAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter() {
        //CustomJsonUsernamePasswordAuthenticationFilter 에서 인증할 객체(Authentication) 생성
        CustomLoginAuthenticationFilter customJsonUsernamePasswordLoginFilter
                = new CustomLoginAuthenticationFilter(objectMapper,jwtService);

        //일반 로그인 인증 로직
        customJsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager()); // 인증 매니저 설정
        customJsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler()); // 로그인 성공 시 핸들러 설정
        customJsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler()); // 로그인 실패 시 핸들러 설정
        return customJsonUsernamePasswordLoginFilter;
    }

    // CustomLogin (2)
    @Bean
    public AuthenticationManager authenticationManager() { // 스프링 시큐리티의 인증을 처리한다.
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        //비밀번호 인코딩
        provider.setPasswordEncoder(passwordEncoder());
        //loginService loadUserByUsername 호출 이때 DaoAuthenticationProvider 가 username 을 꺼내서 loadUserByUsername 을 호출
        provider.setUserDetailsService(loginService);
        // loadUserByUsername 에서 전달받은 UserDetails 에서 password를 추출해 내부의 PasswordEncoder 에서 password 가 일치하는지 검증을 수행
        return new ProviderManager(provider);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationProcessingFilter() {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService, userRepository,adminUserRepository);
        return jwtAuthenticationFilter;
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userRepository,adminUserRepository);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

