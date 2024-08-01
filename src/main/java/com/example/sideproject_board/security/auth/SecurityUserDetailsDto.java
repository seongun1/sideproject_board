package com.example.sideproject_board.security.auth;

import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@AllArgsConstructor
@Slf4j
@Getter
public class SecurityUserDetailsDto implements UserDetails {
    private final User user;
    private Collection<? extends GrantedAuthority> authorities;

    @Override // 계정 만료 여부 : true ->만료 안됨. false : 만료
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override // 계정 잠김 여부 : ture -> 만료 안됨
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override //비밀번호 만료 여부 true -> 만료 안됨
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override // 사용자 활성화 여부 -> 만료 안됨
    public boolean isEnabled() {
        return true;
    }
    // 유저의 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collectors = new ArrayList<>();
        collectors.add(() -> "ROLE : " + user.getRoleValue());

        return collectors;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getLoginId();
    }
}
