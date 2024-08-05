package com.example.sideproject_board.controller;

import com.example.sideproject_board.domain.Role;
import com.example.sideproject_board.domain.User;
import com.example.sideproject_board.dto.JoinRequest;
import com.example.sideproject_board.dto.LoginRequest;
import com.example.sideproject_board.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/session-login")
public class SessionLoginController {

    private final UserService userService;

    @GetMapping(value = {"", "/"})
    public String home(Model model, @SessionAttribute(name = "memberId", required = false) Long userId) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        User loginMember = userService.getLoginUserById(userId);

        if (loginMember != null) {
            model.addAttribute("name", loginMember.getNickname());
        }

        return "home";
    }

    @GetMapping("/join")
    public String joinPage(Model model) {

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        // 회원가입을 위해서 model 통해서 joinRequest 전달
        model.addAttribute("joinRequest", new JoinRequest());
        return "join";
    }

    @PostMapping("/join")
    public String join(@Valid @ModelAttribute JoinRequest joinRequest,
                       BindingResult bindingResult, Model model) {

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        // ID 중복 여부 확인
        if (userService.checkIsDuplicate(joinRequest.getLoginId())) {
            bindingResult.addError(new FieldError("joinRequest", "loginId", "ID가 존재합니다."));
        }

        // 비밀번호 = 비밀번호 체크 여부 확인
        if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
            bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다."));
        }

        // 에러가 존재할 시 다시 join.html로 전송
        if (bindingResult.hasErrors()) {
            return "join";
        }

        // 에러가 존재하지 않을 시 joinRequest 통해서 회원가입 완료
        userService.securityUserJoin(joinRequest);

        // 회원가입 시 홈 화면으로 이동
        return "redirect:/session-login";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        model.addAttribute("loginRequest", new LoginRequest());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult,
                        HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        User member = userService.login(loginRequest);

        if (member == null) {
            bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다.");
        }

        if (bindingResult.hasErrors()) {
            return "login";
        }

        httpServletRequest.getSession().invalidate();
        HttpSession httpSession = httpServletRequest.getSession(true);

        httpSession.setAttribute("memberId", member.getId());
        httpSession.setMaxInactiveInterval(1800);

        return "redirect:/session-login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest httpServletRequest, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        HttpSession httpSession = httpServletRequest.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }

        return "redirect:/session-login";
    }

    @GetMapping("/info")
    public String memberInfo(@SessionAttribute(name = "memberId", required = false) Long memberId, Model model) {
        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션로그인");

        User loginMember = userService.getLoginUserById(memberId);

        if (loginMember == null) {
            return "redirect:/session-login/login";
        }

        model.addAttribute("member", loginMember);
        return "info";
    }
    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "memberId", required = false) Long memberId, Model model) {

        model.addAttribute("loginType", "session-login");
        model.addAttribute("pageName", "세션 로그인");

        User loginMember = userService.getLoginUserById(memberId);

        if(loginMember == null) {
            return "redirect:/session-login/login";
        }

        if(!loginMember.getRole().equals(Role.ADMIN)) {
            return "redirect:/session-login";
        }

        return "admin";
    }
}