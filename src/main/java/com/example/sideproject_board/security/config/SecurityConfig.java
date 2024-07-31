package com.example.sideproject_board.security.config;

import com.example.sideproject_board.security.auth.CustomUserDetailsService;
import com.example.sideproject_board.security.oauth.CustomOAuth2UserService;
import com.nimbusds.oauth2.sdk.AbstractAuthenticatedRequest;
import com.nimbusds.oauth2.sdk.auth.JWTAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;

// 특정 페이지에 대한 접근을 할때 사용 , DB에 저장되는 비밀번호를 암호화시 시용
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig  {

    private final AuthenticationFailureHandler customFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean // 비밀번호 암호화
    public BCryptPasswordEncoder encoder() {return new BCryptPasswordEncoder();}

//    @Bean // 실제로 인증을 처리하는 인터페이스
//    public AuthenticationManager authenticationManagerBean() throws Exception{
//
//    }


    @Bean  //static 관련 설정 무시. -> 따로 인증 허가를 받지 않아도 접근 가능
    public  WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean //configure 함수 역할
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/resources/**").permitAll()
                        .requestMatchers("/main/rootPage").permitAll()
                        .requestMatchers("/","/auth/**","/posts/read/**","/posts/search/**").permitAll()
                        .requestMatchers("/status","/images/**","/view/join","/auth/join").permitAll()
                        .anyRequest().authenticated() // 어떠한 요청이라도 인증이 필요한 창
                )
                .formLogin(login -> login
                        .loginPage("/auth/login") // 커스텀 로그인 페이지 지정
                        .loginProcessingUrl("/auth/loginProc") // submit받을 url
                        .usernameParameter("userid") // submit할 아이디 //프론트에서 name ="userid" , name ="password"라고 지정해 줘야 함.
                        .passwordParameter("password") // subbit할 비밀번호
                        .successHandler(new SimpleUrlAuthenticationSuccessHandler("/main/rootPage"))
                        .failureHandler(customFailureHandler)
                        .defaultSuccessUrl("/")
                        .permitAll() // 이부분은 모두 허용
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                        .logoutSuccessUrl("/")
                        //.permitAll() // 음 .. 이거 붙여야 하나?
                )
                .oauth2Login(oauth2login -> oauth2login
                        .loginPage("/auth/login")
                        .authorizationEndpoint(authorization -> authorization //사용자 지정 로그인 페이지를 렌더링할 수 있는 a를 제공해야 합니다.
                                // @Controller@RequestMapping("/auth/loginProc")
                                .baseUri("/auth/loginProc"))
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)))
        ;
                return httpSecurity.build();
    }

}
