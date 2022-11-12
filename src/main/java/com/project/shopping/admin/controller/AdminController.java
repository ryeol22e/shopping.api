package com.project.shopping.admin.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.admin.service.AdminService;
import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.display.dto.BannerDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	private final AdminService adminService;

	@GetMapping("/menu")
	public ResponseEntity<List<CodeFieldDTO>> getMenu(CodeFieldDTO param) throws Exception {
		return ResponseEntity.ok(adminService.getAdminMenuList(param));
	}
	
	@PostMapping("/banner/save")
	public ResponseEntity<Boolean> registBanner(BannerDTO param) throws Exception {
		return ResponseEntity.ok(adminService.registBanner(param));
	}
	
}
