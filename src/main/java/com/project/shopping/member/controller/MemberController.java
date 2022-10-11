package com.project.shopping.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/check/login")
	public ResponseEntity<Boolean> checkLogin(HttpServletRequest request) throws Exception {
		return ResponseEntity.ok(memberService.checkLogin(request));
	}

	@GetMapping("/login")
	public ResponseEntity<String> login(@RequestParam(name = "memberId") String memberId, @RequestParam(name = "memberPassword") String memberPassword) throws Exception {
		return ResponseEntity.ok(memberService.loginMember(memberId, memberPassword));
	}

	@PostMapping("/join")
	public ResponseEntity<Boolean> join(@RequestBody MemberDTO member) throws Exception {
		return ResponseEntity.ok(memberService.joinMember(member));
	}

	@PostMapping("/auth/number")
	public ResponseEntity<String> authNumber(@RequestBody MemberDTO member) throws Exception {
		return ResponseEntity.ok(memberService.authNumber(member));
	}
}
