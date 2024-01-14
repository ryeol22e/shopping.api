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

	public List<ProductDTO> getProductList(ProductDTO productDTO) {
		return productRepository.findProductList(productDTO);
	}

	public ProductDTO getProductDetail(String prdtNo) {
		return productRepository.findProduct(prdtNo);
	}

}
