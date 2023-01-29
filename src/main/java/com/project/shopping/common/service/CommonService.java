package com.project.shopping.common.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.repository.CommonRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonService {
	private final CommonRepository commonRepository;

	public List<CodeFieldDTO> getCommonList(CodeFieldDTO param) {
		log.info("common code type : {}", param.getCodeType());
		return commonRepository.findByCodeTypeAndCodeDepthAndUseYn(param.getCodeType(), param.getCodeDepth(), param.getUseYn());
	}
}
