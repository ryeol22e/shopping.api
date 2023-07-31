package com.project.shopping.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.member.dto.MemberDTO;
import com.project.shopping.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<MemberDTO> login(@RequestBody MemberDTO member, HttpServletRequest request) {
		return ResponseEntity.ok(memberService.loginMember(member, request));
	}

	@PostMapping("/join")
	public ResponseEntity<Boolean> join(@RequestBody MemberDTO member) {
		return ResponseEntity.ok(memberService.joinMember(member));
	}

	@PostMapping("/auth/number")
	public ResponseEntity<String> authNumber(@RequestBody MemberDTO member) {
		return ResponseEntity.ok(memberService.authNumber(member));
	}
}
