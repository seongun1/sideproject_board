package com.example.sideproject_board.controller;

import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.JoinRequest;
import com.example.sideproject_board.dto.LoginRequest;
import com.example.sideproject_board.security.jwt.JWTUtil;
import com.example.sideproject_board.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Iterator;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JWTLoginController {
    private final UserService userService;
    private final JWTUtil jwtUtil;

    @GetMapping(value = {"","/"})
    public String home (Model model){
        model.addAttribute("loginType","jwt-login");
        model.addAttribute("pageName","스프링 시큐리티 로그인");

        String loginId = SecurityContextHolder.getContext().getAuthentication().getName();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iter = authorities.iterator();
        GrantedAuthority auth = iter.next();
        String role = auth.getAuthority();

        User loginUser = userService.getLoginUserByloginId(loginId);

        if (loginUser != null) {
            model.addAttribute("name", loginUser.getLoginId());
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {

        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "스프링 시큐리티 JWT 로그인");

        // 회원가입을 위해서 model 통해서 joinRequest 전달
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest,
                       BindingResult bindingResult, Model model) {

        model.addAttribute("loginType", "jwt-login");
        model.addAttribute("pageName", "스프링 시큐리티 JWT 로그인");

        // ID 중복 여부 확인
        if (userService.checkIsDuplicate(joinRequest.getLoginId())) {
            return "ID가 존재합니다.";
        }


        // 비밀번호 = 비밀번호 체크 여부 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            return "비밀번호가 일치하지 않습니다.";
        }

        // 에러가 존재하지 않을 시 joinRequest 통해서 회원가입 완료
        userService.securityUserJoin(joinRequest);

        // 회원가입 시 홈 화면으로 이동
        return "redirect:/jwt-login";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){

        User user = userService.login(loginRequest);

        if(user==null){
            return "ID 또는 비밀번호가 일치하지 않습니다!";
        }

        String token = jwtUtil.createJwt(user.getLoginId(), user.getRoleValue());
        return token;
    }

    @GetMapping("/info")
    public String memberInfo(Authentication auth, Model model) {

        User loginMember = userService.getLoginUserByloginId(auth.getName());

        return "ID : " + loginMember.getLoginId() + "\n닉네임 : " + loginMember.getNickname() + "\nrole : " + loginMember.getRole();
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {

        return "인가 성공!";
    }
}
