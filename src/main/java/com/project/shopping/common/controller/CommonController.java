package com.project.shopping.common.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.service.CommonService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CommonController {
	private final CommonService commonService;

	@GetMapping("/headers")
	public ResponseEntity<List<CodeFieldDTO>> getHeaderList(@RequestParam(name = "codeType") String codeType) throws Exception {
		log.info("commonCode is {}", codeType);
		return ResponseEntity.ok(commonService.getHeaders(codeType));
	}
}
