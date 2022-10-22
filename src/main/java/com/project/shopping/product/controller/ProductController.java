package com.project.shopping.product.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.product.dto.ProductDTO;
import com.project.shopping.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product")
public class ProductController {
	private final ProductService productService;

	@GetMapping("/{cateNo}")
	public ResponseEntity<List<ProductDTO>> getProductList(@PathVariable(name = "cateNo") String cateNo) throws Exception {
		return ResponseEntity.ok(productService.getProductList(cateNo));
	}

	@GetMapping("/{prdtNo}")
	public ResponseEntity<ProductDTO> getProductDetail(@PathVariable(name = "prdtNo") String prdtNo) throws Exception {
		return ResponseEntity.ok(productService.getProductDetail(prdtNo));
	}
}