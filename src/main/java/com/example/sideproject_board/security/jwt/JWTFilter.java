package com.example.sideproject_board.security.jwt;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.CustomSecurityUserDetails;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired private final JWTUtil jwtUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {
        final String authorization = request.getHeader("Authorization");
        //Authorization 헤더 검증.
        if (authorization == null || authorization.isBlank()){
            //System.out.println("token null(토큰이 유효하지 않습니다)" + authorization);
            filterChain.doFilter(request,response);
            return;
        }
        ;
        String token = authorization.split(" ")[1];
         // 유효기간이 만료한 경우
        if (jwtUtil.isExpired(token)){
            System.out.println("token expired");
            filterChain.doFilter(request,response);
            return;
        }
        // 토큰 검증 완료 -> 일시적 session 생성
        // session에 user 정보 설정
        String loginId = jwtUtil.getLoginId(token);
        String role = jwtUtil.getRole(token);

        User user = new User();
        user.setLoginId(loginId);

        user.setPassword("임시 비밀번호");
        user.setRole(Role.valueOf(role));

        CustomSecurityUserDetails customSecurityUserDetails =
                new CustomSecurityUserDetails(user);
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(customSecurityUserDetails, null, customSecurityUserDetails.getAuthorities());

        // 세션에 사용자 등록 -> 일시적으로 user 세션이 생성
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request,response);
    }

}
