package com.project.shopping.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.project.shopping.product.dto.ProductTable;
import com.project.shopping.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductTable> getProductList(ProductTable productDTO) {
		return productRepository.findProductList(productDTO);
	}

	public ProductTable getProductDetail(String prdtNo) {
		return productRepository.findProduct(prdtNo);
	}

}
