package com.project.shopping.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.member.service.MemberService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final MemberService memberService;

	@GetMapping("/check")
	public ResponseEntity<Boolean> authCheck(HttpServletRequest request) {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		String memberId = request.getHeader("MemberId");
		
		if((memberId!=null && !memberId.isEmpty()) && (token==null || "".equals(token))) {
			token = memberService.getRefreshToken(memberId);
			log.info("token is {}", token);
		}

		return ResponseEntity.ok(token==null || "".equals(token) ? false : true);
	}
}
