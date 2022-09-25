package com.project.shopping.display.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shopping.display.dto.CategoryDTO;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryDTO, Long> {
	public List<CategoryDTO> findByUpCateNo(String cateNo);
}
