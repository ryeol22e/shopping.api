package com.project.shopping.display.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.display.dto.CategoryInfo;
import com.project.shopping.display.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	private final CategoryRepository categoryRepository;

	public List<CategoryInfo> getCateList(CategoryInfo param) {
		return categoryRepository.findUpCateList(param);
	}
}
