package com.example.sideproject_board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 로그인 시 데이터 받는 폼
@Getter
@NoArgsConstructor
public class LoginRequest {
    private String loginId;
    private String password;


}
