package com.example.sideproject_board.application.security.auth;

import com.example.sideproject_board.application.dto.UserDto;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.infrastructure.persistence.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    // username이 DB에 있는지 확인함.
    @Override
    public UserDetails loadUserByUsername (String username)throws UsernameNotFoundException{
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.: " + username));

        httpSession.setAttribute("user",new UserDto.Response(user));

        // 시큐리티 세션에 유저 정보 저장.
        return new CustomUserDetails(user);
    }

}
