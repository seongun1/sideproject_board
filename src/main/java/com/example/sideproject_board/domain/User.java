package com.example.sideproject_board.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id; // 유저 id

    @Column(nullable = false,length = 30,unique = true) // 30자리 이내, 중복없이 조건
    private String loginId; // 유저 로그인 아이디

    @Column(nullable = false, unique = true)
    private String nickname; // 닉네임

    @Column(length = 100)
    private String password;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // 계정타입

    // 회원정보 수정
    public void modify(String nickname,String password){
        this.nickname = nickname;
        this.password = password;
    }

    // 소셜로그인시 이미 등록된 회원이라면 수정날짜만 업데이트, 기존 데이터를 보존하도록 예외처리

    public User updateModifiedDate(){
        this.onPreUpdate();
        return this;
    }
    public String getRoleValue(){
        return this.role.getValue();
    }
}
