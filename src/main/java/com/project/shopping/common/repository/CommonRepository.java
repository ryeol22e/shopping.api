package com.project.shopping.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shopping.common.dto.CodeFieldDTO;

@Repository
public interface CommonRepository extends JpaRepository<Long, CodeFieldDTO> {
	public List<CodeFieldDTO> findByCodeTypeList(String codeType);
}
