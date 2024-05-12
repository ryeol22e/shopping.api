package com.project.shopping.product.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.project.shopping.product.dto.ProductInfo;
import com.project.shopping.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductInfo> getProductList(ProductInfo productDTO) {
		return new ArrayList() {
			{
				add(new ProductInfo() {
					{
						setPrdtName("<html></html>");
					}
				});
				add(new ProductInfo() {
					{
						setPrdtName("<html><head><script>alert(\"asfsdfsdf\")</script></head></html>");
					}
				});
			}
		};
		// return productRepository.findProductList(productDTO);
	}

	public ProductInfo getProductDetail(String prdtNo) {
		return productRepository.findProduct(prdtNo);
	}

}
