package com.project.shopping.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.shopping.product.dto.ProductDTO;

@Repository
public interface ProductRepository extends JpaRepository<ProductDTO, Long> {
	public List<ProductDTO> findAllByCateNo(String cateNo);
	public ProductDTO findByPrdtNo(String prdtNo);
}
