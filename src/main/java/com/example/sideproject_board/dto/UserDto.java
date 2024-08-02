package com.example.sideproject_board.dto;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;
@AllArgsConstructor
@Builder
@Data
// 회원가입 시 데이터 받는 폼
public class UserDto {
    @Data // @Data에는 Setter와 Getter가 포함되어 있습니다. --> 굳굳
    @Getter
    public static class Request {
        private Long id; // id -> 유저를 구분하는 회원번호

        @NotBlank(message = "이름을 입력하세요")
        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9-_]{4,20}$", message = "아이디는 특수문자를 제외한 4~20자리여야 합니다.")
        private String loginId;

        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9-_]{2,10}$", message = "닉네임은 특수문자를 제외한 2~10자리여야 합니다.")
        @NotBlank(message = "닉네임은 필수 입력 값입니다.")
        private String nickname;

        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        private String email;

        private Role role;

        // 회원정보 수정
        public User toEntity() {
            User user = User.builder()
                    .id(id)
                    .loginId(loginId)
                    .password(password)
                    .nickname(nickname)
                    .email(email)
                    .role(role.USER)
                    .build();
            return user;
        }
    }

        /**
         * 인증된 사용자 정보를 세션에 저장하기 위한 클래스
         * 세션을 저장하기 위해 User 엔티티 클래스를 직접 사용하게 되면 직렬화를 해야 하는데,
         * 엔티티 클래스에 직렬화를 넣어주면 추후에 다른 엔티티와 연관관계를 맺을시
         * 직렬화 대상에 다른 엔티티까지 포함될 수 있어 성능 이슈 우려가 있기 때문에
         * 세션 저장용 Dto 클래스 생성
         * */

        @Getter
        public static class Response implements Serializable {
            private final Long id;
            private final String loginId;
            private final String nickname;
            private final String password;
            private final String email;
            private final Role role;
            private final String modifiedDate;

            public Response(User user){
                this.id = user.getId();
                this.loginId= user.getLoginId();
                this.nickname = user.getNickname();
                this.password  = user.getPassword();
                this.email = user.getEmail();
                this.role = user.getRole();
                this.modifiedDate = user.getModifiedDate();
            }
        }
}
