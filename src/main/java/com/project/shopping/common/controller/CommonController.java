package com.project.shopping.common.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/common")
public class CommonController {
	private final CommonService commonService;

	@GetMapping("/{codeType}")
	public ResponseEntity<List<CodeFieldDTO>> getHeaderList(
			@PathVariable(name = "codeType", required = true) String codeType, CodeFieldDTO param) {
		log.info("commonCode codeType is {}", codeType);
		log.info("commonCode data is {}", param);
		return ResponseEntity.ok(commonService.getCommonList(param));
	}
}
