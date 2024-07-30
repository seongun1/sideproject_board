package com.example.sideproject_board.controller;
// 회원 관련 controller

import com.example.sideproject_board.dto.UserDto;
import com.example.sideproject_board.security.auth.LoginUser;
import com.example.sideproject_board.security.validator.CustomValidators;
import com.example.sideproject_board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserService userService;


    private final CustomValidators.EmailValidator emailValidator;
    private final CustomValidators.NicknameValidator nicknameValidator;
    private final CustomValidators.UsernameValidator usernameValidator;

    @InitBinder
    public void validatorBinder (WebDataBinder binder){
        binder.addValidators(nicknameValidator);
        binder.addValidators(emailValidator);
        binder.addValidators(usernameValidator);
    }

    @GetMapping("/auth/join")
    public String join() {return "/user/user-join";}

    // 회원가입 api
    @PostMapping("/auth/joinProc")
    public String joinProc(@Valid UserDto.Request dto, Errors errors , Model model) {
        if (errors.hasErrors()){
            // 회원가입 실패시 데이터 값을 유지함/

            model.addAttribute("userDto" , dto);

            // 유효성 통과 못한 필드(부분)와 메세지를 핸들링

            Map<String,String> validatorResult = userService.validateHandling(errors);
            for (String key : validatorResult.keySet()){
                model.addAttribute(key,validatorResult.get(key));
            }

            // 회원가입 페이지로 다시 리턴.
            return "/user/user-join";
        }
        userService.userJoin(dto);
        return "redirect:/auth/login";
    }

    @GetMapping("/auth/login")

    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "exception", required = false) String exception,
                        Model model){
        model.addAttribute("error" , error);
        model.addAttribute("exception" , exception);
        return "/user/user-login";
    }

    // security에서 로그아웃은 기본적으로 post이지만, get으로 우회함.
    @GetMapping("/logout")
    public String logout(HttpServletRequest request , HttpServletResponse response) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }

        return "redirect:/";
    }

    // 회원정보 수정
    @GetMapping("/modify")
    public String modify(@LoginUser UserDto.Response user , Model model){
        if (model != null) {
            model.addAttribute("user" , user);
        }

        return "/user/user-modify";
    }
}
