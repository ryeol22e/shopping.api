package com.project.shopping.admin.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.admin.service.AdminService;
import com.project.shopping.common.dto.CommonField;
import com.project.shopping.display.dto.BannerTable;
import com.project.shopping.product.dto.ProductTable;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
	private final AdminService adminService;


	@GetMapping("/menu")
	public ResponseEntity<List<CommonField>> getMenu(CommonField param) throws Exception {
		return ResponseEntity.ok(adminService.getAdminMenuList(param));
	}

	@PostMapping("/banner/save")
	public ResponseEntity<Boolean> registBanner(BannerTable param) throws Exception {
		return ResponseEntity.ok(adminService.registBanner(param));
	}

	@PostMapping("/product/{prdtNo}")
	public ResponseEntity<Boolean> saveProduct(@PathVariable(name = "prdtNo", required = true) String prdtNo, ProductTable parameter) {
		return ResponseEntity.ok(adminService.saveProduct(parameter));
	}

}
