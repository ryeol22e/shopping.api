package com.project.shopping.display.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.display.dto.CategoryDTO;
import com.project.shopping.display.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryDTO> getCateList(String cateNo) throws Exception {
		return categoryRepository.findByUpCateNo(cateNo);
	}
}
