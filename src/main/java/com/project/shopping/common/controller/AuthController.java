package com.project.shopping.common.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@GetMapping("/check")
	public ResponseEntity<Boolean> authCheck(HttpServletRequest request) throws Exception {
		String token = request.getHeader(HttpHeaders.AUTHORIZATION);
		return ResponseEntity.ok(token==null || "".equals(token) ? false : true);
	}
}
