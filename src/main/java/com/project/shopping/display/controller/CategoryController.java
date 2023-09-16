package com.project.shopping.display.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.display.dto.CategoryDTO;
import com.project.shopping.display.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cate")
public class CategoryController {
	private final CategoryService categoryService;

	@GetMapping("/list")
	public ResponseEntity<List<CategoryDTO>> getCateList(CategoryDTO parameter) {
		log.info("request category list.");
		return ResponseEntity.ok(categoryService.getCateList(parameter));
	}
}
