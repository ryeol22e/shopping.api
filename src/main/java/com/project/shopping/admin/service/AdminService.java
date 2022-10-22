package com.project.shopping.admin.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.common.dto.CodeFieldDTO;
import com.project.shopping.common.service.CommonService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
	private final CommonService commonService;

	public List<CodeFieldDTO> getAdminMenuList(CodeFieldDTO param) throws Exception {
		return commonService.getCommonList(param);
	}
	
}
