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

	public List<ProductDTO> getProductList(ProductDTO productDTO) throws Exception {
		String cateNo = productDTO.getCateNo();
		char useYn = productDTO.getUseYn();
		char dispYn = productDTO.getDispYn();
		
		return productRepository.findAllByCateNoAndUseYnAndDispYn(cateNo, useYn, dispYn);
	}

	public ProductDTO getProductDetail(String prdtNo) throws Exception {
		return productRepository.findByPrdtNo(prdtNo);
	}

	public Boolean saveProduct(ProductDTO parameter) throws Exception {
		ProductDTO data = productRepository.save(parameter);
		boolean result = false;

		if(data!=null) {
			result = true;
		}

		return result;
	}
}
