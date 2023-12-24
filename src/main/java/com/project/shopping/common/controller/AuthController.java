package com.project.shopping.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final MemberService memberService;

	@GetMapping("/check")
	public ResponseEntity<Boolean> authCheck(HttpServletRequest request) {
		return ResponseEntity.ok(memberService.checkToken(request));
	}
}
