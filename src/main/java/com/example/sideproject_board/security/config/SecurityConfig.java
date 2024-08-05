package com.example.sideproject_board.security.config;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.security.jwt.JWTFilter;
import com.example.sideproject_board.security.jwt.JWTUtil;
import com.example.sideproject_board.security.jwt.LoginFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 특정 페이지에 대한 접근을 할때 사용 , DB에 저장되는 비밀번호를 암호화시 시용
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig  {

    private final AuthenticationConfiguration configuration;
    private final JWTUtil jwtUtil;


    @Bean // 비밀번호 암호화
    public BCryptPasswordEncoder encoder() {return new BCryptPasswordEncoder();}

    @Bean // 실제로 인증을 처리하는 인터페이스
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }


    @Bean  //static 관련 설정 무시. -> 따로 인증 허가를 받지 않아도 접근 가능
    public  WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


    @Bean //configure 함수 역할
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
       //  ================== 스프링 시큐리티 jwt 로그인 설정 ==================

        http
                .csrf(AbstractHttpConfigurer::disable);
        http
                .formLogin(AbstractHttpConfigurer::disable);
        http
                .httpBasic((AbstractHttpConfigurer::disable));
        http
                .cors(Customizer.withDefaults());
        http
                .authorizeHttpRequests(request -> request
                        .anyRequest().permitAll());
        //OAuth2.0 로그인 방식 설정
//        http
//                .oauth2Login((auth) -> auth.loginPage("/oauth2-login/login")
//                                .defaultSuccessUrl("/oauth2-login")
//                                        .failureUrl("/oauth2-login/login")
//                                                .permitAll());
        // 폼 로그인 방식 설정.
//        http
//                .formLogin((auth) -> auth.loginPage("/oauth2-login/login")
//                        .loginProcessingUrl("/oauth2-login/loginProc")
//                        .usernameParameter("loginId")
//                        .passwordParameter("password")
//                        .defaultSuccessUrl("/oauth-login")
//                        .failureUrl("/oauth2-login/")
//                        .permitAll());
//        http
////                .logout((auth) -> auth
////                        .logoutUrl("/oauth2-login/logout"));

//        http
//                .authorizeHttpRequests((auth) -> auth
//                        .requestMatchers("/jwt-login", "/jwt-login/", "/jwt-login/login", "/jwt-login/join").permitAll()
//                        .requestMatchers("/jwt-login/admin").hasRole("ADMIN")
//                        .anyRequest().authenticated()
//                );
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .addFilterAt(new LoginFilter(authenticationManager(configuration), jwtUtil), UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);
                return http.build();
    }

}
