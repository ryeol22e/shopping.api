package com.project.shopping.product.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.project.shopping.product.dto.ProductDTO;
import com.project.shopping.product.repository.ProductRepository;
import com.project.shopping.utils.UtilsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
	private final ProductRepository productRepository;

	public List<ProductDTO> getProductList(ProductDTO productDTO) {
		List<ProductDTO> list = productRepository.findProductList(productDTO);

		return list;
	}

	public ProductDTO getProductDetail(String prdtNo) {
		ProductDTO product = productRepository.findProduct(prdtNo);

		return product;
	}

	@Transactional
	public boolean saveProduct(ProductDTO product) {
		boolean result = false;
		MultipartFile image = product.getFile();
		String path = UtilsData.getFileBasePath();

		if(UtilsData.fileUpload(image, path)) {
			String imagePath = path;
			String imageName = image.getOriginalFilename();

			product.setImageData(imagePath, imageName);
			log.info("save product data : {}", product);
			productRepository.save(product);
			result = true;
		}

		return result;
	}
}
