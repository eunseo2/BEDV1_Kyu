package org.prgrms.kyu.controller;

import lombok.RequiredArgsConstructor;
import org.prgrms.kyu.dto.JoinRequest;
import org.prgrms.kyu.entity.UserType;
import org.prgrms.kyu.service.SecurityService;
import org.prgrms.kyu.service.StoreService;
import org.prgrms.kyu.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.naming.AuthenticationException;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final StoreService storeService;
    private final SecurityService securityService;

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (securityService.isAuthenticated()) {
          model.addAttribute("userInfo",
                             userService.getUser(((UserDetails) authentication.getPrincipal()).getUsername()));
        }
        model.addAttribute("stores",storeService.findAll());
        return "/index";
    }

    @GetMapping("/user/signup")
    public String signUp(Model model) {
        if (securityService.isAuthenticated()) return "redirect:/";
        model.addAttribute("joinForm", new JoinRequest());
        return "/user/signUpForm";
    }

    @PostMapping("/user/signup")
    public String signUp(@ModelAttribute("joinForm") JoinRequest joinRequest) {
        userService.join(joinRequest);
        userService.autoLogin(joinRequest);
        return "redirect:/";
    }

    @GetMapping("/user/login")
    public String login(Model model, String logout) {
        if (securityService.isAuthenticated()) return "redirect:/";
        if (logout != null) model.addAttribute("message", "안전하게 로그아웃되었습니다.");
        return "/user/loginForm";
    }

}
