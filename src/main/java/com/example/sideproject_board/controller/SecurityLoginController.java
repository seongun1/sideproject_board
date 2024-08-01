package com.example.sideproject_board.controller;

import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.JoinRequest;
import com.example.sideproject_board.dto.LoginRequest;
import com.example.sideproject_board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.Banner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;
import java.util.Iterator;

@Controller
@RequiredArgsConstructor
@RequestMapping("/security-login")
public class SecurityLoginController {
    private final UserService userService;

    @GetMapping(value = {"","/"})
    public String home(Model model){
        model.addAttribute("loginType", "security-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인");

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();

        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        User loginUser = userService.getLoginUserByloginId(loginId);

        if (loginUser != null){
            model.addAttribute("name",loginUser.getLoginId());
        }
        return "home";
    }
    @GetMapping("/join")
    public String joinPage(Model model){
        model.addAttribute("loginType","security-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인.");

        // 회원가입을 위해 model을 통해서 joinRequest 전달.

        model.addAttribute("joinRequest",new JoinRequest());
        return "join";
    }

    @GetMapping("/login")
    public String loginPage(Model model){
        model.addAttribute("loginType","security-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인.");

        model.addAttribute("loginRequest",new LoginRequest());

        return "login";
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth , Model model){
        model.addAttribute("loginType","security-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인.");

        User loginUser = userService.getLoginUserByloginId(auth.getName());

        model.addAttribute("user",loginUser);
        return "info";
    }
    @GetMapping("/admin")
    public String adminPage(Model model){
        model.addAttribute("loginType","security-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인.");

        return "admin";
    }
}
