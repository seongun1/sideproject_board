package com.example.sideproject_board.security.auth;

import com.example.sideproject_board.domain.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final User user;

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
        collectors.add(() -> "ROLE : " + user.getRole());

        return collectors;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }
    @Override
    public String getUsername() {
        return user.getUsername();
    }
}
