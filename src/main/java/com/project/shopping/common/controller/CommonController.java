package com.project.shopping.common.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/common")
@RequiredArgsConstructor
public class CommonController {
	private final CommonService commonService;

	@GetMapping("/headers")
	public ResponseEntity<List<CodeFieldDTO>> getHeaderList(CodeFieldDTO param) throws Exception {
		log.info("commonCode data is {}", param);
		return ResponseEntity.ok(commonService.getCommonList(param));
	}
}
