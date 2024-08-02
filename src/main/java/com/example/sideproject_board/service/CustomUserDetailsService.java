package com.example.sideproject_board.service;

import com.example.sideproject_board.dto.CustomSecurityUserDetails;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@AllArgsConstructor //DB에서 가져온 유저 정보를 시큐리티한테 넘겨주는 클래스
@Component
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    // username이 DB에 있는지 확인함.
    @Override
    public UserDetails loadUserByUsername (String loginId)throws UsernameNotFoundException{
        User loginUser = userRepository.findByLoginId(loginId).orElseThrow(() ->
                new UsernameNotFoundException("해당 사용자가 존재하지 않습니다.: " + loginId));

        if (loginUser != null){
            return new CustomSecurityUserDetails(loginUser);
        }
       return null;
    }
}
