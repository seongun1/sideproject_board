package com.example.sideproject_board.testdb;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TestController {
    private final TestService testService;
    @GetMapping(value = "/api/test")
    public String test(){
        String json = "{name: 'icecream'}";
        return json;
    }
    @GetMapping(value = "/api/user")
    public List<User> getUserList(){{
            return testService.getUserList();
        }
    }
    @GetMapping(value = "/api/user/{userId}")
    public String getUser  (@PathVariable Long userId){
        return testService.getUser(userId);
    }
}
