package com.example.sideproject_board.security.oauth;


import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

//OAuth DTO class

@Slf4j
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class OAuthAttributes {
    private Map<String,Object> attributes;
    private String nameAttributekey;
    private String loginId;
    private String nickname;
    private String email;
    private Role role;

    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes){
        // 구글 // 네이버 // 카카오 구분하기 위한 메소드 (ofnaver , ofkakao)
        if ("naver".equals(registrationId)){
            return ofNaver("id",attributes);
        }
        return ofGoogle (userNameAttributeName,attributes);
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName,
                                     Map<String, Object> attributes){
       return OAuthAttributes.builder()
               .loginId((String) attributes.get("loginId"))
               .email((String) attributes.get("email"))
               .nickname((String) attributes.get("name"))
               .attributes(attributes)
               .nameAttributekey(userNameAttributeName)
               .build();
    }
    public static OAuthAttributes ofNaver(String userNameAttributeName,
                                           Map<String, Object> attributes){
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");

        log.info("naver response: " +response);

        return OAuthAttributes.builder()
                .loginId((String) attributes.get("loginId"))
                .email((String) attributes.get("email"))
                .nickname((String) attributes.get("name"))
                .attributes(attributes)
                .nameAttributekey(userNameAttributeName)
                .build();
    }
    public User toEntity(){
        return User.builder()
                .loginId(loginId)
                .email(email)
                .nickname(nickname)
                .role(Role.SOCIAL)
                .build();
    }
}
