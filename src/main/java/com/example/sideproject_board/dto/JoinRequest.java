package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
// 회원가입 시 데이터 받는 폼
@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {
    @NotBlank(message = "ID를 입력하세요")
    private String loginId;

    @NotBlank(message = "비밀번호를 입력하세요")
    private String password;
    private String passwordCheck;

    @NotBlank(message = "이름을 입력하세요")
    private String nickname;

    public User toEntity(){
        return User.builder()
                .loginId(this.loginId)
                .password(this.password)
                .nickname(this.nickname)
                .role(Role.USER)
                .build();
    }
}
