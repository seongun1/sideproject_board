package com.example.sideproject_board.infrastructure.persistence;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.JoinRequest;
import com.example.sideproject_board.repository.UserRepository;
import com.example.sideproject_board.service.UserService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserRepositoryTest {
    @Autowired private UserRepository userRepository;
    @Autowired private BCryptPasswordEncoder encoder;
    @Autowired
    UserService userService;
    @After
    public void clear() {userRepository.deleteAll();}

    @Test
    public void 유저_생성_가져오기(){
        clear();
        String loginId ="test1";
        String rawPassword = "123!@#qwe";
        String encPassword = encoder.encode(rawPassword);
        userRepository.save(User.builder()
                .loginId(loginId)
                .password(encPassword)
                .nickname("testuser")
                .email("test@naver.com")
                .role(Role.USER).build());

        List<User> userList = userRepository.findAll();

        User user = userList.get(0);

        assertThat(user.getLoginId()).isEqualTo(loginId);
        assertThat(user.getPassword()).isEqualTo(encPassword);

    }
    //
    @Configuration
    @ComponentScan(basePackages = "com.example.sideproject_board")
    static class TestConfig{
        @Bean
        @Primary
        public BCryptPasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
        }
    }
}