package com.example.sideproject_board.security.oauth2;

import io.jsonwebtoken.lang.Objects;
import lombok.AllArgsConstructor;

import java.util.Map;


@AllArgsConstructor
public class KakaoUserDetails implements OAuth2UserInfo{
    private Map<String , Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map)attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String)((Map)attributes.get("properties")).get("nickname");
    }
}
