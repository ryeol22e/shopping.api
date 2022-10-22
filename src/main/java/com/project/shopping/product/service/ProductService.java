package com.project.shopping.product.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.project.shopping.product.dto.ProductDTO;
import com.project.shopping.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductDTO> getProductList(String cateNo) throws Exception {
		return productRepository.findAllByCateNo(cateNo);
	}

	public ProductDTO getProductDetail(String prdtNo) throws Exception {
		return productRepository.findByPrdtNo(prdtNo);
	}
}
