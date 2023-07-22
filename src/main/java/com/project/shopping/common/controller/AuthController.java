package com.project.shopping.common.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	@GetMapping("/check")
	public ResponseEntity<Boolean> authCheck(HttpServletRequest request) {
		return ResponseEntity.ok(request.getSession().getAttribute("memberInfo")==null ? false : true);
	}
}
