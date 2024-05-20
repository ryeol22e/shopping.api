package com.project.shopping.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.member.dto.MemberInfo;
import com.project.shopping.member.service.LoginService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(@RequestBody MemberInfo param) {
        return ResponseEntity.ok(loginService.loginMemberProcess(param));
    }

    @GetMapping("/logout")
    public ResponseEntity<Boolean> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("LOGIN_ID", null);

        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.ok(true);
    }
}
