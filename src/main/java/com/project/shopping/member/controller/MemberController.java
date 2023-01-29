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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberController {
	private final MemberService memberService;

	@GetMapping("/login")
	public ResponseEntity<MemberDTO> login(@RequestParam(name = "memberId") String memberId, @RequestParam(name = "memberPassword") String memberPassword) {
		return ResponseEntity.ok(memberService.loginMember(memberId, memberPassword));
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
