package com.example.sideproject_board.testdb;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TestService {
    private TestUserRepository userRepository;

    public List<TestUser> getUserList() {
        return userRepository.findAll();
    }
    public String getUser(Long userId){
        return "{\n" +
                "\t\t\"id\": 1,\n" +
                "\t\t\"name\": \"김길동\",\n" +
                "\t\t\"age\": 16,\n" +
                "\t\t\"학교\": \"길동중\"\n" +
                "\t}";
    }
}
